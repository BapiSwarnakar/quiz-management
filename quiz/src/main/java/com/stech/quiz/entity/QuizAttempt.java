package com.stech.quiz.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.List;

@Data
@Entity
@Table(name = "quiz_attempts")
public class QuizAttempt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    private User user;
    
    @ManyToOne
    private Quiz quiz;
    
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean completed;
    
    @ElementCollection
    @CollectionTable(name = "quiz_answers")
    @MapKeyColumn(name = "question_id")
    @Column(name = "answer_id")
    private Map<Long, Long> userAnswers; // QuestionId -> AnswerId

    @ElementCollection
    @CollectionTable(name = "quiz_attempt_question_order")
    @OrderColumn(name = "position")
    @Column(name = "question_id")
    private List<Long> questionOrder; // Shuffled question IDs
}
