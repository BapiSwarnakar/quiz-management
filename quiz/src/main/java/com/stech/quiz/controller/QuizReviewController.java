package com.stech.quiz.controller;

import com.stech.quiz.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/quiz/review")
@RequiredArgsConstructor
public class QuizReviewController {
    private final QuizService quizService;

    @GetMapping("/{resultId}")
    public String reviewQuiz(@PathVariable Long resultId, Model model) {
        model.addAttribute("result", quizService.getQuizResult(resultId));
        model.addAttribute("answers", quizService.getUserAnswers(resultId));
        return "quiz/review";
    }
}
