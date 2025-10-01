package dev.evolting.questionservice.services;

import dev.evolting.questionservice.dtos.QuestionDTO;
import dev.evolting.questionservice.entities.Question;
import dev.evolting.questionservice.entities.Response;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface QuestionService {
    public ResponseEntity<List<Question>> getAllQuestion();
    public ResponseEntity<List<Question>> getQuestionsByCategory(String category);
    public ResponseEntity<String> addQuestion(Question question);
    public ResponseEntity<List<Integer>> getQuestionsforQuiz(String category, Integer numQ);
    ResponseEntity<List<QuestionDTO>> getQuestionsByIds(List<Integer> questionIds);
    ResponseEntity<Integer> getScore(List<Response> responses);
}
