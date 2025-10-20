package com.example.fnb.category.domain.web;


import com.example.fnb.category.CategoryService;
import com.example.fnb.category.dto.CategoryDto;
import com.example.fnb.category.dto.CategoryCreateDto;
import com.example.fnb.shared.enums.UserRole;
import com.example.fnb.shared.utils.SecurityUtil;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

}
