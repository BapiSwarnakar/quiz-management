package com.stech.quiz.dto;

import lombok.Data;
import java.util.List;

@Data
public class QuizDto {
    private Long id;
    private String title;
    private String description;
    private List<QuestionDto> questions;
    private Integer totalQuestions;
    private Integer totalMarks;
    private Integer timeInMinutes;
    private Boolean active;
    private String categoryName;
}