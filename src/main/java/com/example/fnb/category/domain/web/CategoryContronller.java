package com.example.fnb.category.domain.web;


import com.example.fnb.category.CategoryService;
import com.example.fnb.category.dto.CategoryDto;
import com.example.fnb.category.dto.CategoryCreateDto;
import com.example.fnb.shared.enums.UserRole;
import com.example.fnb.shared.utils.SecurityUtil;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class CategoryContronller {
    private final CategoryService categoryService;

    public  CategoryContronller(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/api/categories")
    public CategoryDto createCategory(@RequestBody @Valid CategoryCreateDto dto) {
        SecurityUtil.onlyAllowRoles(UserRole.ADMIN);
        return categoryService.createCategory(dto);
    }

    @GetMapping("/api/categories")
    public List<CategoryDto> getAllCategories() {
        return categoryService.getRootCategories();
    }


    @GetMapping("/api/categories/{id}")
    public CategoryDto getAllCategoriesById(@PathVariable UUID id) {
        return categoryService.getCategoryById(id);
    }

    @GetMapping("/api/categories/by-slug/{slug}")
    public CategoryDto getAllCategoriesBySlug(@PathVariable String slug) {
        return categoryService.getCategoryBySlug(slug);
    }
}
