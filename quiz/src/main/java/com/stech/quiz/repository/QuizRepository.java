package com.stech.quiz.repository;

import com.stech.quiz.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
    List<Quiz> findByCategoryId(Long categoryId);
    List<Quiz> findByActive(Boolean active);
}