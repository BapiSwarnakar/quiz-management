package com.stech.quiz.controller;

import com.stech.quiz.service.QuizService;
import com.stech.quiz.entity.Quiz;
import com.stech.quiz.entity.QuizResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@Controller
@RequestMapping("/quiz")
@RequiredArgsConstructor
public class QuizTakingController {
    private final QuizService quizService;

    @GetMapping("/start/{id}")
    public String startQuiz(@PathVariable Long id, Model model) {
        Quiz quiz = quizService.getQuizById(id);
        model.addAttribute("quiz", quiz);
        return "quiz/take-quiz";
    }

    @PostMapping("/submit/{id}")
    public String submitQuiz(@PathVariable Long id, 
                           @RequestParam Map<String, String> answers) {
        QuizResult result = quizService.evaluateQuiz(id, answers);
        return "redirect:/quiz/result/" + result.getId();
    }

    @GetMapping("/result/{resultId}")
    public String showResult(@PathVariable Long resultId, Model model) {
        model.addAttribute("result", quizService.getQuizResult(resultId));
        return "quiz/result";
    }
}
