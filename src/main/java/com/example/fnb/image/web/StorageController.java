package com.example.fnb.image.web;

import com.example.fnb.image.StorageService;
import com.example.fnb.image.dto.FileDataDto;
import com.example.fnb.shared.enums.UserRole;
import com.example.fnb.shared.security.SecurityUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
public class StorageController {

    private final StorageService storageService;

    public StorageController(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping("/api/storage/files/upload")
    public ResponseEntity<FileDataDto> upload(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.status(HttpStatus.CREATED).body(storageService.uploadFile(file));
    }

    @GetMapping("/api/storage/files")
    public ResponseEntity<List<FileDataDto>> getAllFileData() {
        SecurityUtil.onlyAllowRoles(UserRole.ADMIN);
        return ResponseEntity.ok(storageService.getAllFileData());
    }
}