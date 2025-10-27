package com.example.fnb.image.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "images")
@Getter
@Setter
public class Image {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private String storagePath;

    @Column
    private String contentType;

    @Column
    private Long size;

    @Column(nullable = false)
    private Instant uploadedAt;

    @Column
    private UUID uploadedBy;
}
