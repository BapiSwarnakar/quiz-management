package com.stech.quiz.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "quiz_results")
public class QuizResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;
    
    private Integer totalMarks;
    private Integer obtainedMarks;
    private Integer correctAnswers;
    private Integer wrongAnswers;
    private Integer notAttempted;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
}
