package com.stech.quiz.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.stech.quiz.security.UserPrincipal;

@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal UserPrincipal userPrincipal, Model model) {
        model.addAttribute("user", userPrincipal);
        return "dashboard";
    }
}