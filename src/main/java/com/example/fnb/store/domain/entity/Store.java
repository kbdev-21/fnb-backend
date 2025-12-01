package com.example.fnb.store.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "stores")
@Getter
@Setter
public class Store {
    @Id
    private UUID id;

    @Column(unique = true, nullable = false, updatable = false)
    private String code;

    @Column(nullable = false)
    private String displayName;

    @Column(nullable = false, unique = true)
    private String phoneNum;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String fullAddress;

    @Column(nullable = false)
    private boolean open;

    @Column(nullable = false)
    private Instant createdAt;
}
