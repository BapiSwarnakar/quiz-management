package com.stech.quiz.repository;

import com.stech.quiz.entity.QuizResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuizResultRepository extends JpaRepository<QuizResult, Long> {
    List<QuizResult> findByUserId(Long userId);
    
    List<QuizResult> findByQuizId(Long quizId);
    
    List<QuizResult> findTop5ByUserIdOrderByEndTimeDesc(Long userId);
    
    List<QuizResult> findTop10ByOrderByEndTimeDesc();
    
    @Query("SELECT COUNT(r) FROM QuizResult r WHERE r.status = :status")
    long countByStatus(String status);
    
    @Query("SELECT COUNT(r) FROM QuizResult r")
    long countAll();
    
    @Query("SELECT r FROM QuizResult r")
    List<Object[]> findAllQuizReports();
}
