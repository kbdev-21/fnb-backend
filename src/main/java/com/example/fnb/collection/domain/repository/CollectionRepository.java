package com.example.fnb.collection.domain.repository;

import com.example.fnb.collection.domain.entity.Collection;
import com.example.fnb.collection.dto.CollectionDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CollectionRepository extends JpaRepository<Collection, UUID>{
    Optional<Collection> findBySlug(String slug);
}
