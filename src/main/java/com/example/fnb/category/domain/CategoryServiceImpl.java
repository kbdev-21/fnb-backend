package com.example.fnb.category.domain;

import com.example.fnb.category.CategoryService;
import com.example.fnb.category.domain.entity.Category;
import com.example.fnb.category.domain.repository.CategoryRepository;
import com.example.fnb.category.dto.CategoryDto;
import com.example.fnb.category.dto.CategoryCreateDto;
import com.example.fnb.shared.utils.CreatedSlugUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CategoryDto createCategory(CategoryCreateDto dto) {
        String slug = CreatedSlugUtil.createSlug(dto.getName());

        Category newCategory = new Category();
        newCategory.setId(UUID.randomUUID());
        newCategory.setName(dto.getName());
        newCategory.setSlug(slug);
        newCategory.setDescription(dto.getDescription());
        newCategory.setImgUrl(dto.getImgUrl());
        newCategory.setCreatedAt(Instant.now());

        if(dto.getChildrenIds() != null) {
            List<Category> children = categoryRepository.findAllById(dto.getChildrenIds());
            for(Category child : children) {
                child.setParent(newCategory);
            }
            newCategory.setChildren(children);
        }

        Category savedCategory = categoryRepository.save(newCategory);

        return modelMapper.map(savedCategory, CategoryDto.class);
    }

    @Override
    public CategoryDto getCategoryById(UUID id) {
        return null;
    }

    @Override
    public List<CategoryDto> getRootCategories() {
        List<Category> roots = categoryRepository.findByParentIsNull();
        return roots.stream().map(root -> modelMapper.map(root, CategoryDto.class)).toList();
    }

}
