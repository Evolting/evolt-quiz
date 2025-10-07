package dev.evolting.questionservice.services.impl;

import dev.evolting.questionservice.dtos.QuestionDTO;
import dev.evolting.questionservice.entities.Question;
import dev.evolting.questionservice.entities.Response;
import dev.evolting.questionservice.repositories.QuestionRepository;
import dev.evolting.questionservice.services.QuestionService;
import io.github.resilience4j.retry.annotation.Retry;
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
import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;

    private static final Logger log = LoggerFactory.getLogger(QuestionServiceImpl.class);

    private final StreamBridge streamBridge;

    @Cacheable(value = "questions", key = "'allQuestions'")
    public List<Question> getAllQuestion() {
        try{
            return questionRepository.findAll();
        } catch (Exception e){
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Cacheable(value = "questions", key = "#category")
    @Override
    public List<Question> getQuestionsByCategory(String category) {
        try{
            return questionRepository.findQuestionsByCategory(category);
        } catch (Exception e){
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @CacheEvict(value = "questions", allEntries = true)
    @Override
    public String addQuestion(Question question) {
        try{
            Boolean isAdded = questionRepository.save(question) != null;
            return isAdded ?
                    "Question Added Successfully"
                    : "Question Not Added";
        } catch (Exception e){
            e.printStackTrace();
        }
        return "Error";
    }

    @Retry(name = "getQuestionsforQuiz", fallbackMethod = "getQuestionsforQuizFallback")
    @Override
    public List<Integer> getQuestionsforQuiz(String category, Integer numQ) {
        List<Integer> questionIds = questionRepository.findRandomQuestionsByCategory(category, numQ);

        notifyQuestionSetGenerated(questionIds);

        return questionIds;
    }

    public List<Integer> getQuestionsforQuizFallback(String category, Integer numQ, Throwable throwable) {
        log.error("Error in getting questions for quiz", throwable);

        List<Integer> questionIds = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0));

        return questionIds;
    }

    private void notifyQuestionSetGenerated(List<Integer> questionIds) {
        log.info("Notify generated question set with the details: {}", questionIds);
        var result = streamBridge.send("notifyQuestionSetGenerated-out-0", questionIds);
        log.info("Is the sending Question set request successfully triggered ? : {}", result);
    }

    @Override
    public List<QuestionDTO> getQuestionsByIds(List<Integer> questionIds) {
        List<QuestionDTO> questionDTOS = new ArrayList<>();
        for (Integer questionId : questionIds) {
            Question question = questionRepository.findById(questionId).get();
            QuestionDTO questionDTO = new QuestionDTO();
            questionDTO.setId(question.getId());
            questionDTO.setQuestionTitle(question.getQuestionTitle());
            questionDTO.setOption1(question.getOption1());
            questionDTO.setOption2(question.getOption2());
            questionDTO.setOption3(question.getOption3());
            questionDTO.setOption4(question.getOption4());
            questionDTOS.add(questionDTO);
        }
        return questionDTOS;
    }

    @Override
    public Integer getScore(List<Response> responses) {
        int rightAnswers = 0;
        for (Response response : responses) {
            Question question = questionRepository.findById(response.getId()).get();
            if(response.getResponse().equals(question.getRightAnswer())){
                rightAnswers++;
            }
        }
        return rightAnswers;
    }
}
