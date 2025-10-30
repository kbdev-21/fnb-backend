package com.example.fnb.category.domain;

import com.example.fnb.category.CategoryService;
import com.example.fnb.category.domain.entity.Category;
import com.example.fnb.category.domain.repository.CategoryRepository;
import com.example.fnb.category.dto.CategoryDto;
import com.example.fnb.category.dto.CategoryCreateDto;
import com.example.fnb.category.dto.CategoryUpdateDto;
import com.example.fnb.shared.exception.DomainException;
import com.example.fnb.shared.exception.DomainExceptionCode;
import com.example.fnb.shared.utils.StringUtil;
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
        String slug = StringUtil.createSlug(dto.getName());
        String normalizedName = StringUtil.normalizeVietnamese(dto.getName());

        Category newCategory = new Category();
        newCategory.setId(UUID.randomUUID());
        newCategory.setName(dto.getName());
        newCategory.setSlug(slug);
        newCategory.setNormalizedName(normalizedName);
        newCategory.setDescription(dto.getDescription());
        newCategory.setImgUrl(dto.getImgUrl());
        newCategory.setCreatedAt(Instant.now());

        // === Trường hợp 3: Gán category con vào cha ===
        if (dto.getParentId() != null) {
            Category parentCategory = categoryRepository.findById(dto.getParentId())
                    .orElseThrow(() -> new DomainException(DomainExceptionCode.CATEGORY_NOT_FOUND));
            newCategory.setParent(parentCategory);
        }

        // Lưu category mới (bắt buộc làm trước khi gán vào children)
        Category savedCategory = categoryRepository.save(newCategory);

        // === Trường hợp 2: Gán category cha cho các con ===
        if (dto.getChildrenIds() != null && !dto.getChildrenIds().isEmpty()) {
            List<Category> children = categoryRepository.findAllById(dto.getChildrenIds());
            for (Category child : children) {
                child.setParent(savedCategory); // Gán cha mới cho con
            }
            categoryRepository.saveAll(children);
            savedCategory.setChildren(children); // Nếu bạn cần dùng ở DTO
        }

        return modelMapper.map(savedCategory, CategoryDto.class);
    }

    @Override
    public CategoryDto getCategoryById(UUID id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new DomainException(DomainExceptionCode.CATEGORY_NOT_FOUND));
        return modelMapper.map(category, CategoryDto.class);
    }

    @Override
    public List<CategoryDto> getCategoriesByIdIns(List<UUID> ids) {
        List<Category> categories = categoryRepository.findAllById(ids);
        if (categories.size() != ids.size()) {
            throw new DomainException(DomainExceptionCode.CATEGORY_NOT_FOUND);
        }
        return categories.stream()
            .map(category -> modelMapper.map(category, CategoryDto.class))
            .toList();
    }

    @Override
    public CategoryDto getCategoryBySlug(String slug) {
        Category category = categoryRepository.findBySlug(slug).orElseThrow(() ->  new DomainException(DomainExceptionCode.CATEGORY_NOT_FOUND));
        return modelMapper.map(category, CategoryDto.class);
    }

    @Override
    public List<CategoryDto> getRootCategories() {
        List<Category> roots = categoryRepository.findByParentIsNull();
        return roots.stream().map(root -> modelMapper.map(root, CategoryDto.class)).toList();
    }

    @Override
    public CategoryDto updateCategory(UUID id, CategoryUpdateDto dto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new DomainException(DomainExceptionCode.CATEGORY_NOT_FOUND));

        if (dto.getName() != null) {
            String slug = StringUtil.createSlug(dto.getName());
            String normalizedName = StringUtil.normalizeVietnamese(dto.getName());
            category.setName(dto.getName());
            category.setSlug(slug);
            category.setNormalizedName(normalizedName);
        }

        if (dto.getDescription() != null) {
            category.setDescription(dto.getDescription());
        }

        if (dto.getImgUrl() != null) {
            category.setImgUrl(dto.getImgUrl());
        }

        categoryRepository.save(category);

        return modelMapper.map(category, CategoryDto.class);
    }

}
