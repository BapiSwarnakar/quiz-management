package com.stech.quiz.service;

import com.stech.quiz.entity.Question;
import com.stech.quiz.entity.Answer;
import com.stech.quiz.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;

    @Transactional
    public Question saveQuestion(Question question, Integer correctAnswerIndex) {
        if (correctAnswerIndex != null && correctAnswerIndex >= 0 
            && correctAnswerIndex < question.getAnswers().size()) {
            question.setCorrectAnswer(question.getAnswers().get(correctAnswerIndex));
        }
        return questionRepository.save(question);
    }

    public void deleteQuestion(Long id) {
        questionRepository.deleteById(id);
    }

    public Question getQuestion(Long id) {
        return questionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Question not found"));
    }
}
