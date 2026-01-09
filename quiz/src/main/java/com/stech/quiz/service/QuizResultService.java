package com.stech.quiz.service;

import com.stech.quiz.entity.QuizResult;
import com.stech.quiz.repository.QuizResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuizResultService {
    private final QuizResultRepository quizResultRepository;
    
    public QuizResult saveResult(QuizResult result) {
        if (result.getEndTime() == null) {
            result.setEndTime(LocalDateTime.now());
        }
        return quizResultRepository.save(result);
    }
    
    public List<QuizResult> getUserResults(Long userId) {
        return quizResultRepository.findByUserId(userId);
    }
    
    public java.util.Optional<QuizResult> getResultByAttemptId(Long attemptId) {
        return quizResultRepository.findByAttemptId(attemptId);
    }
    
    public QuizResult getResult(Long resultId) {
        return quizResultRepository.findById(resultId)
            .orElseThrow(() -> new RuntimeException("Result not found"));
    }
}
