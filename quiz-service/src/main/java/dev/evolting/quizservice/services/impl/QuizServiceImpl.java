package dev.evolting.quizservice.services.impl;

import dev.evolting.quizservice.dtos.QuestionDTO;
import dev.evolting.quizservice.dtos.QuizDTO;
import dev.evolting.quizservice.dtos.QuizMsgDTO;
import dev.evolting.quizservice.entities.Quiz;
import dev.evolting.quizservice.entities.Response;
import dev.evolting.quizservice.feign.QuestionInterface;
import dev.evolting.quizservice.repositories.QuizRepository;
import dev.evolting.quizservice.services.QuizService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class QuizServiceImpl implements QuizService {

    private final QuizRepository quizRepository;
    private final QuestionInterface questionInterface;

    private static final Logger log = LoggerFactory.getLogger(QuizServiceImpl.class);

    private final StreamBridge  streamBridge;

    @Cacheable(value = "quiz", key = "'allQuizzes'")
    @Override
    public List<Quiz> getAllQuiz() {
        return quizRepository.findAll();
    }

    @Cacheable(value = "quiz", key = "#id")
    @Override
    public Quiz getQuizById(Integer id) {
        Optional<Quiz> quiz = quizRepository.findById(id);
        if (!quiz.isPresent()) {
            return null;
        }
        return quiz.get();
    }

    @CacheEvict(value = "quiz", key = "'allQuizzes'")
    @Override
    public String addQuiz(String category, Integer numQ, String title) {
        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        Quiz savedQuiz = quizRepository.save(quiz);

        notifyAddNewQuiz(new QuizMsgDTO(savedQuiz.getId(), category, numQ, title));

        return "Quiz Added Successfully";
    }

    private void notifyAddNewQuiz(QuizMsgDTO quizMsgDTO) {
        log.info("Notify adding new Quiz for the details: {}", quizMsgDTO);
        var result = streamBridge.send("notifyAddNewQuiz-out-0", quizMsgDTO);
        log.info("Is the adding new Quiz request successfully triggered ? : {}", result);
    }

    @Override
    public String updateQuizQuestions(Integer id, List<Integer> questionIds) {
        log.info("Adding these questions: {}", questionIds);

        Optional<Quiz> quiz = quizRepository.findById(id);
        if (!quiz.isPresent()) {
            return "Quiz not found";
        }
        quiz.get().setQuestionIds(questionIds);
        quizRepository.save(quiz.get());

        return "Quiz Question set updated Successfully";
    }

    @Override
    public List<QuestionDTO> getQuizQuestions(Integer id) {
        Quiz quiz = quizRepository.findById(id).get();
        List<Integer> questionIds = quiz.getQuestionIds();
        List<QuestionDTO> questionDTOS = questionInterface.getQuestionsByIds(questionIds).getBody();

        return questionDTOS;
    }

    @Override
    public Integer calculateResult(List<Response> responses) {
        int rightAnswers = 0;
        rightAnswers = questionInterface.getScore(responses).getBody();
        return rightAnswers;
    }
}
