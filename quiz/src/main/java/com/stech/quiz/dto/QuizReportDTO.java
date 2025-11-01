package com.stech.quiz.dto;

import lombok.Data;

@Data
public class QuizReportDTO {
    private Long quizId;
    private String quizTitle;
    private int totalAttempts;
    private double averageScore;
    private double passRate;
}
