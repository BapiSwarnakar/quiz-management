package com.stech.quiz.controller;

import com.stech.quiz.service.QuizService;
import com.stech.quiz.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class QuizController {
    private final QuizService quizService;
    private final CategoryService categoryService;

    @GetMapping("/quiz/list")
    public String quizList(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        return "quiz/dashboard";
    }

    @GetMapping("/quiz/{id}")
    public String startQuiz(@PathVariable Long id, Model model) {
        model.addAttribute("quiz", quizService.getQuizById(id));
        return "quiz/take-quiz";
    }

}