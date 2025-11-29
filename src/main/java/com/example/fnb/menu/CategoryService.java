package com.example.fnb.menu;

import com.example.fnb.menu.domain.entity.Category;
import com.example.fnb.menu.dto.CategoryCreateDto;
import com.example.fnb.menu.dto.CategoryDto;
import com.example.fnb.menu.dto.CategoryUpdateDto;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    CategoryDto createCategory(CategoryCreateDto createCategory);
    CategoryDto getCategoryById(UUID id);
    Category getCategoryEntityById(UUID id);
    List<CategoryDto> getCategoriesByIdIns(List<UUID> ids);
    CategoryDto getCategoryBySlug(String slug);
    List<CategoryDto> getRootCategories();
    CategoryDto updateCategory(UUID id, CategoryUpdateDto categoryDto);
    CategoryDto deleteCategory(UUID id);
}
