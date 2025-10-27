package com.example.fnb.image;

import com.example.fnb.image.dto.ImageCreateDto;
import com.example.fnb.image.dto.ImageDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface ImageService {
    ImageDto createImage(ImageCreateDto dto, MultipartFile file);
    ImageDto getImageById(UUID id);
    List<ImageDto> getAllImages();
}
