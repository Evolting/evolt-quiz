package dev.evolting.quizservice.services;

import dev.evolting.quizservice.dtos.QuestionDTO;
import dev.evolting.quizservice.entities.Quiz;
import dev.evolting.quizservice.entities.Response;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface QuizService {
    List<Quiz> getAllQuiz();
    Quiz getQuizById(Integer id);
    String addQuiz(String category, Integer numQ, String title);
    String updateQuizQuestions(List<Integer> questionIds);
    List<QuestionDTO> getQuizQuestions(Integer id);
    Integer calculateResult(List<Response> responses);
}
