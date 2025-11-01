package com.stech.quiz.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
@Table(name = "quizzes")
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String title;
    private String description;
    private Integer timeInMinutes;
    private Integer totalMarks;
    private Boolean active = true;
    
    @ManyToOne
    @JoinColumn(name = "category_id")
    private QuizCategory category;
    
    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL)
    private List<Question> questions;
}
