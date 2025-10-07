package dev.evolting.questionservice.services;

import dev.evolting.questionservice.dtos.QuestionDTO;
import dev.evolting.questionservice.entities.Question;
import dev.evolting.questionservice.entities.Response;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface QuestionService {
    public List<Question> getAllQuestion();
    public List<Question> getQuestionsByCategory(String category);
    public String addQuestion(Question question);
    public List<Integer> getQuestionsforQuiz(Integer id, String category, Integer numQ);
    List<QuestionDTO> getQuestionsByIds(List<Integer> questionIds);
    Integer getScore(List<Response> responses);
}
