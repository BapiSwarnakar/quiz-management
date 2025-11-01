package com.stech.quiz.repository;

import com.stech.quiz.entity.QuizCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizCategoryRepository extends JpaRepository<QuizCategory, Long> {
}
