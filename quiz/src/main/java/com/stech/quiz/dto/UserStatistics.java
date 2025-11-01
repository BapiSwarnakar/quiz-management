package com.stech.quiz.dto;

import com.stech.quiz.entity.QuizResult;
import lombok.Data;
import java.util.List;

@Data
public class UserStatistics {
    private int totalQuizzes;
    private double averageScore;
    private long totalTimeSpent;
    private List<QuizResult> recentResults;
}
