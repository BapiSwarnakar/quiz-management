package com.stech.quiz.controller;

import com.stech.quiz.entity.QuizAttempt;
import com.stech.quiz.service.QuizAttemptService;
import com.stech.quiz.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/quiz/attempt")
@RequiredArgsConstructor
public class QuizAttemptController {
    private final QuizAttemptService attemptService;
    private final QuizService quizService;

    @PostMapping("/start/{quizId}")
    public String startQuiz(@PathVariable Long quizId, Model model) {
        QuizAttempt attempt = attemptService.startQuiz(
            quizService.getQuizById(quizId), 
            // TODO: Get current user from security context
            null
        );
        return "redirect:/quiz/attempt/" + attempt.getId();
    }

    @GetMapping("/{attemptId}")
    public String showQuiz(@PathVariable Long attemptId, Model model) {
        model.addAttribute("attempt", attemptService.getAttempt(attemptId));
        return "quiz/attempt";
    }
}
