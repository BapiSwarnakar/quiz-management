package com.stech.quiz.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.stech.quiz.entity.QuizCategory;
import com.stech.quiz.service.CategoryService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('CATEGORY_VIEW')")
    public String listCategories(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        return "admin/categories/list";
    }

    @GetMapping("/create")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('CATEGORY_CREATE')")
    public String createCategoryForm(Model model) {
        model.addAttribute("category", new QuizCategory());
        return "admin/categories/form";
    }

    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('CATEGORY_CREATE')")
    public String saveCategory(@ModelAttribute QuizCategory category, RedirectAttributes redirectAttributes) {
        try {
            categoryService.saveCategory(category);
            redirectAttributes.addFlashAttribute("success", "Category saved successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to save category: " + e.getMessage());
        }
        return "redirect:/admin/categories";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('CATEGORY_EDIT')")
    public String editCategoryForm(@PathVariable Long id, Model model) {
        QuizCategory category = categoryService.getCategoryById(id);
        model.addAttribute("category", category);
        return "admin/categories/form";
    }

    @PostMapping("/delete")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('CATEGORY_DELETE')")
    public String deleteCategory(@RequestParam("categoryId") Long categoryId, RedirectAttributes redirectAttributes) {
        try {
            categoryService.deleteCategory(categoryId);
            redirectAttributes.addFlashAttribute("success", "Category deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete category: " + e.getMessage());
        }
        return "redirect:/admin/categories";
    }
}
