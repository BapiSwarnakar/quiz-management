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

    @PostMapping("/save/{quizId}")
    public String saveQuestion(@PathVariable Long quizId,
                             @ModelAttribute Question question,
                             @RequestParam(required = false) Integer correctAnswerIndex) {
        question.setQuiz(quizService.getQuizById(quizId));
        questionService.saveQuestion(question, correctAnswerIndex);
        return "redirect:/admin/questions/quiz/" + quizId;
    }

    @GetMapping("/edit/{id}")
    public String editQuestionForm(@PathVariable Long id, Model model) {
        Question question = questionService.getQuestion(id);
        model.addAttribute("quiz", question.getQuiz());
        model.addAttribute("question", question);
        return "admin/questions/form";
    }

    @PostMapping("/update/{id}")
    public String updateQuestion(@PathVariable Long id,
                               @ModelAttribute Question question,
                               @RequestParam(required = false) Integer correctAnswerIndex) {
        Question existingQuestion = questionService.getQuestion(id);
        question.setId(id);
        question.setQuiz(existingQuestion.getQuiz());
        questionService.saveQuestion(question, correctAnswerIndex);
        return "redirect:/admin/questions/quiz/" + existingQuestion.getQuiz().getId();
    }

    @PostMapping("/delete/{id}")
    public String deleteQuestion(@PathVariable Long id) {
        Question question = questionService.getQuestion(id);
        Long quizId = question.getQuiz().getId();
        questionService.deleteQuestion(id);
        return "redirect:/admin/questions/quiz/" + quizId;
    }
}
