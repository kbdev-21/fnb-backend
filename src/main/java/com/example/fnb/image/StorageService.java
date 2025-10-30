package com.example.fnb.image;

import com.example.fnb.image.dto.FileDataDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface StorageService {
    FileDataDto uploadFile(MultipartFile file);
    List<FileDataDto> getAllFileData();
}