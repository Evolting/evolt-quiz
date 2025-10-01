package dev.evolting.questionservice.controllers;

import dev.evolting.questionservice.dtos.QuestionDTO;
import dev.evolting.questionservice.entities.Question;
import dev.evolting.questionservice.entities.Response;
import dev.evolting.questionservice.services.QuestionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/question")
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping("/all-question")
    public ResponseEntity<List<Question>> getAllQuestion() {
        return questionService.getAllQuestion();
    }

    @GetMapping("/all-question/{category}")
    public ResponseEntity<List<Question>> getQuestionsByCategory(@PathVariable String category) {
        return questionService.getQuestionsByCategory(category);
    }

    @PostMapping
    public ResponseEntity<String> addQuestion(@RequestBody Question question) {
        return questionService.addQuestion(question);
    }

    @GetMapping("generate")
    public ResponseEntity<List<Integer>> getQuestionsforQuiz(@RequestParam String categoryName, @RequestParam Integer numQ) {
        return questionService.getQuestionsforQuiz(categoryName, numQ);
    }

    @PostMapping("get-by-ids")
    public ResponseEntity<List<QuestionDTO>> getQuestionsByIds(@RequestBody List<Integer> questionIds) {
        return questionService.getQuestionsByIds(questionIds);
    }

    @PostMapping("get-score")
    public ResponseEntity<Integer> getScore(@RequestBody List<Response> responses) {
        return questionService.getScore(responses);
    }
}
