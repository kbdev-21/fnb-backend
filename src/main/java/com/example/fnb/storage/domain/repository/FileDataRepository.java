package com.example.fnb.storage.domain.repository;

import com.example.fnb.storage.domain.entity.FileData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FileDataRepository extends JpaRepository<FileData, UUID> {
}
