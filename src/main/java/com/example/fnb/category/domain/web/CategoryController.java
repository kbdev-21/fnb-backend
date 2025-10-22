package com.example.fnb.category.domain.web;


import com.example.fnb.category.CategoryService;
import com.example.fnb.category.dto.CategoryDto;
import com.example.fnb.category.dto.CategoryCreateDto;
import com.example.fnb.shared.enums.UserRole;
import com.example.fnb.shared.utils.SecurityUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/api/categories")
    public ResponseEntity<CategoryDto> createCategory(@RequestBody @Valid CategoryCreateDto dto) {
        SecurityUtil.onlyAllowRoles(UserRole.ADMIN);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createCategory(dto));
    }

    @GetMapping("/api/categories")
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getRootCategories());
    }

    @GetMapping("/api/categories/{id}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable UUID id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @GetMapping("/api/categories/by-slug/{slug}")
    public ResponseEntity<CategoryDto> getCategoryBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(categoryService.getCategoryBySlug(slug));
    }
}
