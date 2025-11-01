package com.stech.quiz.service;

import com.stech.quiz.entity.*;
import com.stech.quiz.repository.QuizRepository;
import com.stech.quiz.repository.QuizResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class QuizService {
    private final QuizRepository quizRepository;
    private final QuizResultRepository resultRepository;

    public List<Quiz> getActiveQuizzes() {
        return quizRepository.findByActive(true);
    }

    public Quiz getQuizById(Long id) {
        return quizRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Quiz not found"));
    }

    @Transactional
    public QuizResult evaluateQuiz(Long quizId, Map<String, String> answers) {
        Quiz quiz = getQuizById(quizId);
        QuizResult result = new QuizResult();
        // Implement quiz evaluation logic
        return resultRepository.save(result);
    }

    public long getQuizCount() {
        return quizRepository.count();
    }

    public long getActiveQuizCount() {
        return quizRepository.findByActive(true).size();
    }

    public List<Quiz> findAllQuizzes() {
        return quizRepository.findAll();
    }

    public void deleteQuizById(Long quizId) {
        quizRepository.deleteById(quizId);
    }

    public QuizResult getResult(Long resultId) {
        return resultRepository.findById(resultId)
            .orElseThrow(() -> new RuntimeException("Result not found"));
    }

    public QuizResult getQuizResult(Long resultId) {
        return getResult(resultId);
    }

    public Map<String, String> getUserAnswers(Long resultId) {
        // TODO: Implement user answers retrieval logic
        return Map.of();
    }
}