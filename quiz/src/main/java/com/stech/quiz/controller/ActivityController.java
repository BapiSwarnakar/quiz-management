package com.stech.quiz.controller;

import com.stech.quiz.service.UserActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/activity")
@RequiredArgsConstructor
public class ActivityController {
    private final UserActivityService activityService;

    @GetMapping
    public String viewActivities(Model model) {
        model.addAttribute("activities", activityService.getRecentActivities());
        model.addAttribute("userStats", activityService.getUserStatistics());
        return "admin/activity-dashboard";
    }

    @GetMapping("/user/{userId}")
    public String viewUserActivities(@PathVariable Long userId, Model model) {
        model.addAttribute("activities", activityService.getUserActivities(userId));
        return "admin/user-activities";
    }
}
