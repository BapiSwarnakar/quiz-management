package com.stech.quiz.controller;

import com.stech.quiz.monitoring.QuizMetricsCollector;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/monitoring")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class MonitoringController {
    private final QuizMetricsCollector metricsCollector;

    @GetMapping
    public String showDashboard(Model model) {
        model.addAttribute("metrics", metricsCollector.getMetrics());
        return "admin/monitoring/dashboard";
    }
}
