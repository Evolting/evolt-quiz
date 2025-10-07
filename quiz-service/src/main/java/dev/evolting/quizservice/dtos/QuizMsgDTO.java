package dev.evolting.quizservice.dtos;

import java.util.List;

public record QuizMsgDTO(Integer id, String category, Integer numQ, String title) {
}
