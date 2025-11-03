package com.stech.quiz.service;

import com.stech.quiz.entity.*;
import com.stech.quiz.repository.QuizAttemptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class QuizAttemptService {
    private final QuizAttemptRepository attemptRepository;
    private final QuizResultService resultService;

    @Transactional
    public QuizAttempt startQuiz(Quiz quiz, User user) {
        QuizAttempt attempt = new QuizAttempt();
        attempt.setQuiz(quiz);
        attempt.setUser(user);
        attempt.setStartTime(LocalDateTime.now());
        attempt.setCompleted(false);
        // Initialize shuffled question order
        var ids = quiz.getQuestions().stream().map(Question::getId).collect(java.util.stream.Collectors.toList());
        java.util.Collections.shuffle(ids);
        attempt.setQuestionOrder(ids);
        return attemptRepository.save(attempt);
    }

    @Transactional
    public QuizResult submitQuiz(Long attemptId, Map<Long, Long> answers) {
        QuizAttempt attempt = attemptRepository.findById(attemptId)
            .orElseThrow(() -> new RuntimeException("Attempt not found"));
            
        attempt.setUserAnswers(answers);
        attempt.setEndTime(LocalDateTime.now());
        attempt.setCompleted(true);
        
        return calculateResult(attempt);
    }

    @Transactional
    public void saveAnswer(Long attemptId, Long questionId, Long answerId) {
        QuizAttempt attempt = attemptRepository.findById(attemptId)
            .orElseThrow(() -> new RuntimeException("Attempt not found"));
        Map<Long, Long> ua = attempt.getUserAnswers();
        if (ua == null) ua = new HashMap<>();
        if (answerId == null) {
            ua.remove(questionId);
        } else {
            ua.put(questionId, answerId);
        }
        attempt.setUserAnswers(ua);
        attemptRepository.save(attempt);
    }

    @Transactional
    public QuizResult finalizeAttempt(Long attemptId) {
        QuizAttempt attempt = attemptRepository.findById(attemptId)
            .orElseThrow(() -> new RuntimeException("Attempt not found"));
        attempt.setEndTime(LocalDateTime.now());
        attempt.setCompleted(true);
        return calculateResult(attempt);
    }

    private QuizResult calculateResult(QuizAttempt attempt) {
        Quiz quiz = attempt.getQuiz();
        int totalQuestions = quiz.getQuestions().size();
        int correctAnswers = 0;
        int totalMarks = 0;
        
        for (Question question : quiz.getQuestions()) {
            Long userAnswerId = attempt.getUserAnswers().get(question.getId());
            if (userAnswerId != null && userAnswerId.equals(question.getCorrectAnswer().getId())) {
                correctAnswers++;
                totalMarks += question.getMarks();
            }
        }
        
        QuizResult result = new QuizResult();
        result.setUser(attempt.getUser());
        result.setQuiz(quiz);
        result.setTotalMarks(quiz.getTotalMarks());
        result.setObtainedMarks(totalMarks);
        result.setCorrectAnswers(correctAnswers);
        result.setWrongAnswers(totalQuestions - correctAnswers);
        result.setStartTime(attempt.getStartTime());
        result.setEndTime(attempt.getEndTime());
        
        return resultService.saveResult(result);
    }

    public QuizAttempt getAttempt(Long attemptId) {
        return attemptRepository.findById(attemptId)
            .orElseThrow(() -> new RuntimeException("Attempt not found"));
    }
}
