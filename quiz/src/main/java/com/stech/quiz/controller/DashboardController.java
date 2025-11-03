package com.stech.quiz.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.stech.quiz.security.UserPrincipal;
import com.stech.quiz.service.QuizService;
import com.stech.quiz.service.CategoryService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class DashboardController {
    private final QuizService quizService;
    private final CategoryService categoryService;

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal UserPrincipal userPrincipal, Model model) {
        model.addAttribute("user", userPrincipal);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("activeQuizzes", quizService.getActiveQuizzes());
        return "dashboard";
    }
}