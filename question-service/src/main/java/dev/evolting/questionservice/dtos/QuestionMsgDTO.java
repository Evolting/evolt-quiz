package dev.evolting.questionservice.dtos;

import java.util.List;

public record QuestionMsgDTO(Integer id, List<Integer> questionIds) {
}
