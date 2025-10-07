package dev.evolting.quizservice.controllers;

import dev.evolting.quizservice.dtos.QuestionDTO;
import dev.evolting.quizservice.dtos.QuizDTO;
import dev.evolting.quizservice.entities.Quiz;
import dev.evolting.quizservice.entities.Response;
import dev.evolting.quizservice.services.QuizService;
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

    @PostMapping
    public ResponseEntity<String> createQuiz(@RequestBody QuizDTO quizDTO){
        return new ResponseEntity<>(quizService.addQuiz(quizDTO.getCategory(), quizDTO.getNumQ(), quizDTO.getTitle()), HttpStatus.CREATED);
    }

    @PostMapping("submit")
    public ResponseEntity<Integer> calculateResult(@RequestBody List<Response> responses){
        return new ResponseEntity<>(quizService.calculateResult(responses), HttpStatus.OK);
    }
}
