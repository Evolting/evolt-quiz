package dev.evolting.quizservice.services;

import dev.evolting.quizservice.dtos.QuestionDTO;
import dev.evolting.quizservice.entities.Quiz;
import dev.evolting.quizservice.entities.Response;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface QuizService {
    ResponseEntity<List<Quiz>> getAllQuiz();
    ResponseEntity<Quiz> getQuizById(Integer id);
    ResponseEntity<String> addQuiz(String category, Integer numQ, String title);
    ResponseEntity<String> updateQuizQuestions(List<Integer> questionIds);
    ResponseEntity<List<QuestionDTO>> getQuizQuestions(Integer id);
    ResponseEntity<Integer> calculateResult(List<Response> responses);
}
