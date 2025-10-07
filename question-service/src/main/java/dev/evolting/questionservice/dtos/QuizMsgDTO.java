package dev.evolting.questionservice.dtos;

import java.util.List;

public record QuizMsgDTO(Integer id, String category, Integer numQ, List<Integer> questionIds) {
}

