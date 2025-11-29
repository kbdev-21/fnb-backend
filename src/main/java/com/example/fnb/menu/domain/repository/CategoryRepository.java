package com.example.fnb.menu.domain.repository;

import com.example.fnb.menu.domain.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
    Optional<Category> findBySlug(String slug);
    List<Category> findByParentIsNull();
    List<Category> findByIdIn(List<UUID> ids);
    boolean existsByParent_Id(@Param("parentId") UUID parentId);
}
