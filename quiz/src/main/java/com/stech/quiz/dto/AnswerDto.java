package com.stech.quiz.dto;

import lombok.Data;

@Data
public class AnswerDto {
    private Long id;
    private String answerText;
    private Boolean isCorrect;
}
