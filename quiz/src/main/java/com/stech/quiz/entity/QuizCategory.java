package com.stech.quiz.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
@Table(name = "quiz_categories")
public class QuizCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String description;
    
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Quiz> quizzes;
}
