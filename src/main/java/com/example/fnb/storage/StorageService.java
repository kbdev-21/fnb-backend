package com.example.fnb.storage;

import com.example.fnb.storage.dto.FileDataDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StorageService {
    FileDataDto uploadFile(MultipartFile file);
    List<FileDataDto> getAllFileData();
}