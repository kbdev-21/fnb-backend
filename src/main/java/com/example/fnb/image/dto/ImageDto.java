package com.example.fnb.image.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ImageDto {
    private UUID id;
    private String fileName;
    private String url;
    private String storagePath;
    private String contentType;
    private Long size;
    private Instant uploadedAt;
    private UUID uploadedBy;
}
