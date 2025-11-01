package com.stech.quiz.controller;

import com.stech.quiz.entity.User;
import com.stech.quiz.service.UserService;
import com.stech.quiz.service.QuizService;
import com.stech.quiz.service.CategoryService;
import com.stech.quiz.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;
    private final QuizService quizService;
    private final CategoryService categoryService;
    private final StatisticsService statisticsService;

    @GetMapping
    public String adminDashboard(Model model) {
        model.addAttribute("userCount", userService.getUserCount());
        model.addAttribute("quizCount", quizService.getQuizCount());
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("recentResults", statisticsService.getRecentResults());
        return "admin/dashboard";
    }

    @GetMapping("/users")
    public String listUsers(Model model) {
        List<User> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "admin/users";
    }

    @PostMapping("/users/delete")
    public String deleteUser(@RequestParam("userId") Long userId) {
        userService.deleteUserById(userId);
        return "redirect:/admin/users";
    }

    @GetMapping("/quizzes")
    public String listQuizzes(Model model) {
        model.addAttribute("quizzes", quizService.findAllQuizzes());
        return "admin/quizzes";
    }

    @PostMapping("/quizzes/delete")
    public String deleteQuiz(@RequestParam("quizId") Long quizId) {
        quizService.deleteQuizById(quizId);
        return "redirect:/admin/quizzes";
    }
}