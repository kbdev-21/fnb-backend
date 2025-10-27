package com.example.fnb.image.domain;

import com.example.fnb.image.ImageService;
import com.example.fnb.image.domain.entity.Image;
import com.example.fnb.image.domain.repository.ImageRepository;
import com.example.fnb.image.dto.ImageCreateDto;
import com.example.fnb.image.dto.ImageDto;
import com.example.fnb.shared.security.SecurityUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final ModelMapper modelMapper;

    public ImageServiceImpl(ImageRepository imageRepository, ModelMapper modelMapper) {
        this.imageRepository = imageRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ImageDto createImage(ImageCreateDto dto, MultipartFile file) {
        Image image = modelMapper.map(dto, Image.class);

        image.setId(UUID.randomUUID());
        image.setUploadedAt(Instant.now());
        SecurityUtil.getCurrentUserId().ifPresent(image::setUploadedBy);

        image.setUrl(dto.getStoragePath());

        Image saved = imageRepository.save(image);
        return modelMapper.map(saved, ImageDto.class);
    }

    @Override
    public ImageDto getImageById(UUID id) {
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy ảnh"));
        return modelMapper.map(image, ImageDto.class);
    }

    @Override
    public List<ImageDto> getAllImages() {
        return imageRepository.findAll()
                .stream()
                .map(img -> modelMapper.map(img, ImageDto.class))
                .collect(Collectors.toList());
    }
}
