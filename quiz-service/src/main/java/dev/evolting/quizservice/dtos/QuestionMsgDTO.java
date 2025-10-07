package dev.evolting.quizservice.dtos;

import java.util.List;

public record QuestionMsgDTO(Integer id, List<Integer> questionIds) {
}
