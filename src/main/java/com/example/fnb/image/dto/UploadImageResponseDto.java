package com.example.fnb.image.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UploadImageResponseDto {
    private String imgUrl;
    private String imgFileName;
}