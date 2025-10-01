package dev.evolting.quizservice.feign;

import dev.evolting.quizservice.dtos.QuestionDTO;
import dev.evolting.quizservice.entities.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "QUESTION-SERVICE")
public interface QuestionInterface {
    @GetMapping("question/generate")
    public ResponseEntity<List<Integer>> getQuestionsforQuiz(@RequestParam String categoryName, @RequestParam Integer numQ);

    @PostMapping("question/get-by-ids")
    public ResponseEntity<List<QuestionDTO>> getQuestionsByIds(@RequestBody List<Integer> questionIds);

    @PostMapping("question/get-score")
    public ResponseEntity<Integer> getScore(@RequestBody List<Response> responses);
}
