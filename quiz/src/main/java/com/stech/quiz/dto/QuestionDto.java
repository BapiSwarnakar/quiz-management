package com.stech.quiz.dto;

import lombok.Data;
import java.util.List;

@Data
public class QuestionDto {
    private Long id;
    private String questionText;
    private List<AnswerDto> answers;
    private Integer marks;
    private String difficulty;
}
