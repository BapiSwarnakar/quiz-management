package com.stech.quiz.service;

import com.stech.quiz.entity.Question;
import com.stech.quiz.entity.Answer;
import com.stech.quiz.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;

    @Transactional
    public Question saveQuestion(Question question, Integer correctAnswerIndex) {
        // Normalize answers: keep up to 4 non-empty options, set back-references
        List<Answer> provided = question.getAnswers() != null ? question.getAnswers() : new ArrayList<>();
        List<Answer> normalized = provided.stream()
                .filter(a -> a != null && a.getContent() != null && !a.getContent().trim().isEmpty())
                .limit(4)
                .collect(Collectors.toList());
        for (Answer a : normalized) {
            a.setQuestion(question);
        }
        question.setAnswers(normalized);

        // Set correct answer if index valid and the option has content
        if (correctAnswerIndex != null && correctAnswerIndex >= 0 && correctAnswerIndex < normalized.size()) {
            Answer candidate = normalized.get(correctAnswerIndex);
            if (candidate.getContent() != null && !candidate.getContent().trim().isEmpty()) {
                question.setCorrectAnswer(candidate);
            } else {
                question.setCorrectAnswer(null);
            }
        } else {
            question.setCorrectAnswer(null);
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
