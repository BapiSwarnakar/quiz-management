package com.stech.quiz.controller;

import com.stech.quiz.entity.Question;
import com.stech.quiz.service.QuizService;
import com.stech.quiz.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/questions")
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;
    private final QuizService quizService;

    @GetMapping("/quiz/{quizId}")
    public String listQuestions(@PathVariable Long quizId, Model model) {
        model.addAttribute("quiz", quizService.getQuizById(quizId));
        return "admin/questions/list";
    }

    @GetMapping("/add/{quizId}")
    public String addQuestionForm(@PathVariable Long quizId, Model model) {
        model.addAttribute("quiz", quizService.getQuizById(quizId));
        model.addAttribute("question", new Question());
        return "admin/questions/form";
    }
}
