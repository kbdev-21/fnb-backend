package com.example.fnb.collection.domain.entity;


import com.example.fnb.image.domain.entity.Image;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Table
@Getter
@Setter
public class Collection {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column (nullable = false)
    private String slug;

    @Column (nullable = false)
    private int sortOrder;

    @Nullable
    private String description;

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private int productsCount;

    @ManyToOne
    @JoinColumn(name = "image_id")
    private Image image;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "collectionId", referencedColumnName = "id")
    private List<ProductCollection> productCollections;

}
