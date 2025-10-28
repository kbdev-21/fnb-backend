package com.example.fnb.category;

import com.example.fnb.category.dto.CategoryDto;
import com.example.fnb.category.dto.CategoryCreateDto;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    CategoryDto createCategory(CategoryCreateDto createCategory);
    CategoryDto getCategoryById(UUID id);
    List<CategoryDto> getCategoriesByIdIns(List<UUID> ids);
    CategoryDto getCategoryBySlug(String slug);
    List<CategoryDto> getRootCategories();
}
