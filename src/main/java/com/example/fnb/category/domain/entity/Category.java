package com.example.fnb.category.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "categories")
@Getter
@Setter
public class Category {

    @Id
    private UUID id;

    @Column (nullable = false, unique = true)
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @JsonBackReference
    private Category parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Category> children = new ArrayList<>();

}
