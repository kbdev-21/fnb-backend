package com.example.fnb.image.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ImageCreateDto {

    @NotBlank
    private String fileName;

    @NotBlank
    private String storagePath;

    private String contentType;
    private Long size;
}
