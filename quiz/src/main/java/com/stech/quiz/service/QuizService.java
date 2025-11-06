package com.stech.quiz.service;

import com.stech.quiz.entity.*;
import com.stech.quiz.repository.QuizRepository;
import com.stech.quiz.repository.QuizResultRepository;
import com.stech.quiz.repository.QuizAttemptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Comparator;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class QuizService {
    private final QuizRepository quizRepository;
    private final QuizResultRepository resultRepository;
    private final QuizAttemptRepository attemptRepository;

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

    public Quiz findQuizById(Long id) {
        return quizRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Quiz not found with id: " + id));
    }

    public Quiz saveQuiz(Quiz quiz) {
        return quizRepository.save(quiz);
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

    public Map<Long, Long> getUserAnswers(Long resultId) {
        QuizResult result = getResult(resultId);
        Long userId = result.getUser().getId();
        Long quizId = result.getQuiz().getId();

        // Find user's attempts and select the most relevant completed attempt for this quiz
        List<QuizAttempt> attempts = attemptRepository.findByUserId(userId);
        QuizAttempt matched = attempts.stream()
            .filter(a -> a.getQuiz() != null && Objects.equals(a.getQuiz().getId(), quizId))
            .filter(a -> a.isCompleted() && a.getEndTime() != null)
            .sorted(Comparator.comparing(QuizAttempt::getEndTime).reversed())
            .findFirst()
            .orElse(null);

        return matched != null && matched.getUserAnswers() != null ? matched.getUserAnswers() : java.util.Collections.emptyMap();
    }
}