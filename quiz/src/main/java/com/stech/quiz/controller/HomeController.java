package com.stech.quiz.controller;

import com.stech.quiz.service.QuizService;
import com.stech.quiz.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final QuizService quizService;
    private final CategoryService categoryService;

    @GetMapping("/")
    public String home() {
        return "redirect:/dashboard";
    }

    @GetMapping("/home")
    public String homePage(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("activeQuizzes", quizService.getActiveQuizzes());
        return "home";
    }
}
