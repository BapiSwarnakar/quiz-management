package com.stech.quiz.controller;

import com.stech.quiz.entity.User;
import com.stech.quiz.entity.Role;
import com.stech.quiz.entity.Quiz;
import com.stech.quiz.service.UserService;
import com.stech.quiz.service.QuizService;
import com.stech.quiz.service.CategoryService;
import com.stech.quiz.service.StatisticsService;
import com.stech.quiz.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;
    private final QuizService quizService;
    private final CategoryService categoryService;
    private final StatisticsService statisticsService;
    private final RoleRepository roleRepository;

    @GetMapping("/dashboard")
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

    @GetMapping("/users/new")
    public String newUserForm(Model model) {
        User user = new User();
        List<Role> allRoles = roleRepository.findAll();
        model.addAttribute("user", user);
        model.addAttribute("allRoles", allRoles);
        return "admin/users-new";
    }

    @PostMapping("/users/new")
    public String createUser(@ModelAttribute User user, 
                            @RequestParam(required = false) List<Long> roleIds,
                            @RequestParam String password,
                            RedirectAttributes redirectAttributes) {
        try {
            // Check if email already exists
            if (userService.existsByEmail(user.getEmail())) {
                redirectAttributes.addFlashAttribute("error", "Email already exists!");
                return "redirect:/admin/users/new";
            }
            
            // Set password
            user.setPassword(password);
            
            // Assign roles if provided
            if (roleIds != null && !roleIds.isEmpty()) {
                Set<Role> roles = new HashSet<>();
                for (Long roleId : roleIds) {
                    roleRepository.findById(roleId).ifPresent(roles::add);
                }
                user.setRoles(roles);
            }
            
            userService.createUser(user);
            redirectAttributes.addFlashAttribute("success", "User created successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to create user: " + e.getMessage());
            return "redirect:/admin/users/new";
        }
        return "redirect:/admin/users";
    }

    @GetMapping("/users/edit/{id}")
    public String editUserForm(@PathVariable Long id, Model model) {
        User user = userService.findUserById(id);
        List<Role> allRoles = roleRepository.findAll();
        model.addAttribute("user", user);
        model.addAttribute("allRoles", allRoles);
        return "admin/users-edit";
    }

    @PostMapping("/users/edit/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute User user, 
                            @RequestParam(required = false) List<Long> roleIds,
                            RedirectAttributes redirectAttributes) {
        try {
            User existingUser = userService.findUserById(id);
            existingUser.setName(user.getName());
            existingUser.setEmail(user.getEmail());
            existingUser.setMobile(user.getMobile());
            existingUser.setGender(user.getGender());
            
            // Update roles if provided
            if (roleIds != null && !roleIds.isEmpty()) {
                Set<Role> roles = new HashSet<>();
                for (Long roleId : roleIds) {
                    roleRepository.findById(roleId).ifPresent(roles::add);
                }
                existingUser.setRoles(roles);
            }
            
            userService.updateUser(existingUser);
            redirectAttributes.addFlashAttribute("success", "User updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update user: " + e.getMessage());
        }
        return "redirect:/admin/users";
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

    @GetMapping("/quizzes/create")
    public String createQuizForm(Model model) {
        Quiz quiz = new Quiz();
        model.addAttribute("quiz", quiz);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "admin/quizzes-form";
    }

    @PostMapping("/quizzes/create")
    public String createQuiz(@ModelAttribute Quiz quiz, 
                            @RequestParam(required = false) Long categoryId,
                            RedirectAttributes redirectAttributes) {
        try {
            if (categoryId != null) {
                quiz.setCategory(categoryService.getCategoryById(categoryId));
            }
            quizService.saveQuiz(quiz);
            redirectAttributes.addFlashAttribute("success", "Quiz created successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to create quiz: " + e.getMessage());
            return "redirect:/admin/quizzes/create";
        }
        return "redirect:/admin/quizzes";
    }

    @GetMapping("/quizzes/edit/{id}")
    public String editQuizForm(@PathVariable Long id, Model model) {
        Quiz quiz = quizService.findQuizById(id);
        model.addAttribute("quiz", quiz);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "admin/quizzes-form";
    }

    @PostMapping("/quizzes/edit/{id}")
    public String updateQuiz(@PathVariable Long id, @ModelAttribute Quiz quiz,
                            @RequestParam(required = false) Long categoryId,
                            RedirectAttributes redirectAttributes) {
        try {
            Quiz existingQuiz = quizService.findQuizById(id);
            existingQuiz.setTitle(quiz.getTitle());
            existingQuiz.setDescription(quiz.getDescription());
            existingQuiz.setTimeInMinutes(quiz.getTimeInMinutes());
            existingQuiz.setTotalMarks(quiz.getTotalMarks());
            existingQuiz.setActive(quiz.getActive());
            
            if (categoryId != null) {
                existingQuiz.setCategory(categoryService.getCategoryById(categoryId));
            }
            
            quizService.saveQuiz(existingQuiz);
            redirectAttributes.addFlashAttribute("success", "Quiz updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update quiz: " + e.getMessage());
        }
        return "redirect:/admin/quizzes";
    }

    @PostMapping("/quizzes/delete")
    public String deleteQuiz(@RequestParam("quizId") Long quizId, RedirectAttributes redirectAttributes) {
        try {
            quizService.deleteQuizById(quizId);
            redirectAttributes.addFlashAttribute("success", "Quiz deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete quiz: " + e.getMessage());
        }
        return "redirect:/admin/quizzes";
    }
}