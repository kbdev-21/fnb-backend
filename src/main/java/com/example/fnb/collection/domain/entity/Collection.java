package com.example.fnb.collection.domain.entity;


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

    @Column(nullable = false, unique = true)
    private String name;

    @Column (nullable = false, unique = true)
    private String slug;

    @Column (nullable = false, unique = true)
    private String normalizedName;

    @Column (nullable = false)
    private int sortOrder;

    @Nullable
    private String description;

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private int productsCount;

    @Column(nullable = false)
    private String imgUrl;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "collectionId", referencedColumnName = "id")
    private List<ProductCollection> productCollections;

}
