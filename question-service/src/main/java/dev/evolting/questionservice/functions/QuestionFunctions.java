package dev.evolting.questionservice.functions;

import dev.evolting.questionservice.dtos.QuestionDTO;
import dev.evolting.questionservice.dtos.QuizDTO;
import dev.evolting.questionservice.services.QuestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

@Configuration
public class QuestionFunctions {

    private static final Logger log = LoggerFactory.getLogger(QuestionFunctions.class);
    private final QuestionService questionService;

    public QuestionFunctions(QuestionService questionService) {
        this.questionService = questionService;
    }

    @Bean
    public Consumer<QuizDTO> generateQuestionSet() {
        return quizDTO -> {
            log.info("Generate question set for quiz: " +  quizDTO.toString());

            questionService.getQuestionsforQuiz(quizDTO.getCategory(), quizDTO.getNumQ()).getBody();
        };
    }

}
