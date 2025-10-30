package com.example.fnb.image.domain.repository;

import com.example.fnb.image.domain.entity.FileData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FileDataRepository extends JpaRepository<FileData, UUID> {
}
