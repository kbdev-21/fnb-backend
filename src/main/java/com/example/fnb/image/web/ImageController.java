package com.example.fnb.image.web;

import com.example.fnb.image.ImageService;
import com.example.fnb.image.dto.UploadImageResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/api/images/upload")
    public ResponseEntity<UploadImageResponseDto> upload(@RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(imageService.uploadFile(file));
    }

//    @GetMapping("/api/images")
//    public ResponseEntity<List<Map<String, Object>>> listFiles() {
//        try {
//            return ResponseEntity.ok(supabaseStorageService.listFiles());
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().build();
//        }
//    }
//
//    @GetMapping("/api/images/{fileName}")
//    public ResponseEntity<UploadImageResponseDto> getFile(@PathVariable String fileName) {
//        try {
//            String url = supabaseStorageService.getPublicUrl(fileName);
//            return ResponseEntity.ok(new UploadImageResponseDto(fileName, url, "File found"));
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().body(new UploadImageResponseDto(fileName, null, e.getMessage()));
//        }
//    }

//    @DeleteMapping("/api/images/{fileName}")
//    public ResponseEntity<String> delete(@PathVariable String fileName) {
//        try {
//            supabaseStorageService.deleteFile(fileName);
//            return ResponseEntity.ok("Deleted: " + fileName);
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().body("Delete failed: " + e.getMessage());
//        }
//    }
}