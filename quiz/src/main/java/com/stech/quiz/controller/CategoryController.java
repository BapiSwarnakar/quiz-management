package com.stech.quiz.controller;

import com.stech.quiz.entity.QuizCategory;
import com.stech.quiz.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/categories")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public String listCategories(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        return "admin/categories/list";
    }

    @GetMapping("/create")
    public String createCategoryForm(Model model) {
        model.addAttribute("category", new QuizCategory());
        return "admin/categories/form";
    }

    @PostMapping("/save")
    public String saveCategory(@ModelAttribute QuizCategory category) {
        categoryService.saveCategory(category);
        return "redirect:/admin/categories";
    }
}
