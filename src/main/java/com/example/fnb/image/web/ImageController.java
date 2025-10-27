package com.example.fnb.image.web;

import com.example.fnb.image.ImageService;
import com.example.fnb.image.dto.ImageCreateDto;
import com.example.fnb.image.dto.ImageDto;
import com.example.fnb.shared.enums.UserRole;
import com.example.fnb.shared.security.SecurityUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/api/images")
    public ResponseEntity<ImageDto> createImage(@RequestParam("file") MultipartFile file) {
        SecurityUtil.onlyAllowRoles(UserRole.ADMIN);

        String fileName = file.getOriginalFilename();

        String storagePath = "/uploads/" + UUID.randomUUID() + "_" + fileName;

        ImageCreateDto dto = new ImageCreateDto(fileName, storagePath, file.getContentType(), file.getSize());

        return ResponseEntity.status(HttpStatus.CREATED).body(imageService.createImage(dto, file));
    }

    @GetMapping("/api/images/{id}")
    public ResponseEntity<ImageDto> getImageById(@PathVariable UUID id) {
        return ResponseEntity.ok(imageService.getImageById(id));
    }

    @GetMapping("/api/images")
    public ResponseEntity<List<ImageDto>> getAllImages() {
        return ResponseEntity.ok(imageService.getAllImages());
    }
}
