package com.stech.quiz.controller;

import com.stech.quiz.entity.User;
import com.stech.quiz.service.UserService;
import com.stech.quiz.service.QuizResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {
    private final UserService userService;
    private final QuizResultService quizResultService;

    @GetMapping
    public String viewProfile(Model model) {
        model.addAttribute("user", userService.getCurrentUser());
        model.addAttribute("quizResults", quizResultService.getUserResults(userService.getCurrentUser().getId()));
        return "profile/view";
    }

    @GetMapping("/edit")
    public String editProfile(Model model) {
        model.addAttribute("user", userService.getCurrentUser());
        return "profile/edit";
    }

    @PostMapping("/update")
    public String updateProfile(@ModelAttribute User user, 
                              @RequestParam("photo") MultipartFile photo) {
        userService.updateProfile(user, photo);
        return "redirect:/profile?success";
    }
}
