package com.stech.quiz.service;

import com.stech.quiz.entity.QuizCategory;
import com.stech.quiz.repository.QuizCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final QuizCategoryRepository categoryRepository;

    public List<QuizCategory> getAllCategories() {
        return categoryRepository.findAll();
    }

    public QuizCategory save(QuizCategory category) {
        return categoryRepository.save(category);
    }

    public QuizCategory saveCategory(QuizCategory category) {
        return categoryRepository.save(category);
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    public QuizCategory getCategory(Long id) {
        return categoryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Category not found"));
    }
}
