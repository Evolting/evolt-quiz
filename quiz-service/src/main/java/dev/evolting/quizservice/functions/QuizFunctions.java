package dev.evolting.quizservice.functions;

import dev.evolting.quizservice.services.QuizService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.function.Consumer;

@Configuration
public class QuizFunctions {

    private static final Logger log = LoggerFactory.getLogger(QuizFunctions.class);
    private final QuizService questionService;

    public QuizFunctions(QuizService questionService) {
        this.questionService = questionService;
    }

    @Bean
    public Consumer<List<Integer>> updateQuizQuestions() {
        return questionIds -> {
            log.info("Adding these questions: {}", questionIds);
        };
    }

}
