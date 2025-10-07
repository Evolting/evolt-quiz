package dev.evolting.quizservice.controllers;

import dev.evolting.quizservice.dtos.QuestionDTO;
import dev.evolting.quizservice.dtos.QuizDTO;
import dev.evolting.quizservice.entities.Quiz;
import dev.evolting.quizservice.entities.Response;
import dev.evolting.quizservice.services.QuizService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quiz")
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @GetMapping("/all-quiz")
    public ResponseEntity<List<Quiz>> getAllQuiz(){
        return new ResponseEntity<>(quizService.getAllQuiz(), org.springframework.http.HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Quiz> getQuizById(@PathVariable Integer id){
        return new ResponseEntity<>(quizService.getQuizById(id), org.springframework.http.HttpStatus.OK);
    }

    @GetMapping("get-question/{id}")
    public ResponseEntity<List<QuestionDTO>> getQuizQuestions(@PathVariable Integer id){
        return new ResponseEntity<>(quizService.getQuizQuestions(id), org.springframework.http.HttpStatus.OK);
    }

    @RateLimiter(name = "createQuiz", fallbackMethod = "createQuizFallback")
    @PostMapping
    public ResponseEntity<String> createQuiz(@RequestBody QuizDTO quizDTO){
        return new ResponseEntity<>(quizService.addQuiz(quizDTO.getCategory(), quizDTO.getNumQ(), quizDTO.getTitle()), HttpStatus.CREATED);
    }

    public ResponseEntity<String> createQuizFallback(QuizDTO quizDTO, Throwable throwable){
        return new ResponseEntity<>("Please wait for a few seconds before adding more quiz", HttpStatus.TOO_MANY_REQUESTS);
    }

    @PostMapping("submit")
    public ResponseEntity<Integer> calculateResult(@RequestBody List<Response> responses){
        return new ResponseEntity<>(quizService.calculateResult(responses), HttpStatus.OK);
    }
}
