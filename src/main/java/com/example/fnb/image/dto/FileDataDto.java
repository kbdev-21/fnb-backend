package com.example.fnb.image.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class FileDataDto {
    private UUID id;
    private String publicUrl;
    private String key;
}