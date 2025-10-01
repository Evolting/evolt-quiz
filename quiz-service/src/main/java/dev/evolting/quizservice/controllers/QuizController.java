package dev.evolting.quizservice.controllers;

import dev.evolting.quizservice.dtos.QuestionDTO;
import dev.evolting.quizservice.dtos.QuizDTO;
import dev.evolting.quizservice.entities.Quiz;
import dev.evolting.quizservice.entities.Response;
import dev.evolting.quizservice.services.QuizService;
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
        return quizService.getAllQuiz();
    }

    @GetMapping("{id}")
    public ResponseEntity<Quiz> getQuizById(@PathVariable Integer id){
        return quizService.getQuizById(id);
    }

    @GetMapping("get-question/{id}")
    public ResponseEntity<List<QuestionDTO>> getQuizQuestions(@PathVariable Integer id){
        return quizService.getQuizQuestions(id);
    }

    @PostMapping
    public ResponseEntity<String> createQuiz(@RequestBody QuizDTO quizDTO){
        return quizService.addQuiz(quizDTO.getCategory(), quizDTO.getNumQ(), quizDTO.getTitle());
    }

    @PostMapping("submit")
    public ResponseEntity<Integer> calculateResult(@RequestBody List<Response> responses){
        return quizService.calculateResult(responses);
    }
}
