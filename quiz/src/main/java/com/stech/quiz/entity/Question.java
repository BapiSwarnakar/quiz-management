package com.stech.quiz.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
@Table(name = "questions")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(length = 1000)
    private String content;
    private Integer marks;
    
    @ManyToOne
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;
    
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<Answer> answers;
    
    @OneToOne
    @JoinColumn(name = "correct_answer_id")
    private Answer correctAnswer;
}
