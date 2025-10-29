package com.example.fnb.image;

import com.example.fnb.image.dto.UploadImageResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface ImageService {
    UploadImageResponseDto uploadFile(MultipartFile file) throws IOException;
}