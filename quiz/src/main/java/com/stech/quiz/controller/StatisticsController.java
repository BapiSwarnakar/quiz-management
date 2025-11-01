package com.stech.quiz.controller;

import com.stech.quiz.service.StatisticsService;
import com.stech.quiz.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/statistics")
@RequiredArgsConstructor
public class StatisticsController {
    private final StatisticsService statisticsService;
    private final UserService userService;

    @GetMapping
    public String showStatistics(Model model) {
        Long currentUserId = userService.getCurrentUser().getId();
        model.addAttribute("statistics", statisticsService.getUserStatistics(currentUserId));
        return "dashboard/statistics";
    }
}
