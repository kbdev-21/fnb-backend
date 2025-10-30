package com.example.fnb.storage.dto;


import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class FileDataDto {
    private UUID id;
    private String publicUrl;
    private String key;
}