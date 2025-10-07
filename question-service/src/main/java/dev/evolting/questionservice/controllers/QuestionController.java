package dev.evolting.questionservice.controllers;

import dev.evolting.questionservice.dtos.QuestionDTO;
import dev.evolting.questionservice.entities.Question;
import dev.evolting.questionservice.entities.Response;
import dev.evolting.questionservice.services.QuestionService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.http.HttpStatus;
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
        return new ResponseEntity<>(questionService.getAllQuestion(), HttpStatus.OK);
    }

    @GetMapping("/all-question/{category}")
    public ResponseEntity<List<Question>> getQuestionsByCategory(@PathVariable String category) {
        return new ResponseEntity<>(questionService.getQuestionsByCategory(category), HttpStatus.OK);
    }

    @RateLimiter(name = "addQuestion", fallbackMethod = "addQuestionFallback")
    @PostMapping
    public ResponseEntity<String> addQuestion(@RequestBody Question question) {
        return new ResponseEntity<>(questionService.addQuestion(question), HttpStatus.CREATED);
    }

    public ResponseEntity<String> addQuestionFallback(Question question, Throwable throwable) {
        return new ResponseEntity<>("Please wait for a few seconds before adding more quiz", HttpStatus.TOO_MANY_REQUESTS);
    }

    @GetMapping("generate")
    public ResponseEntity<List<Integer>> getQuestionsforQuiz(@RequestParam Integer id, @RequestParam String categoryName, @RequestParam Integer numQ) {
        return new ResponseEntity<>(questionService.getQuestionsforQuiz(id, categoryName, numQ), HttpStatus.OK);
    }

    @PostMapping("get-by-ids")
    public ResponseEntity<List<QuestionDTO>> getQuestionsByIds(@RequestBody List<Integer> questionIds) {
        return new ResponseEntity<>(questionService.getQuestionsByIds(questionIds), HttpStatus.OK);
    }

    @PostMapping("get-score")
    public ResponseEntity<Integer> getScore(@RequestBody List<Response> responses) {
        return new ResponseEntity<>(questionService.getScore(responses), HttpStatus.OK);
    }
}
