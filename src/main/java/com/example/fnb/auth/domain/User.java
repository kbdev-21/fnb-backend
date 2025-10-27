package com.example.fnb.auth.domain;

import com.example.fnb.shared.enums.UserRole;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.jetbrains.annotations.Nullable;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
    @Id
    private UUID id;

    @Column(nullable = false, unique = true)
    private String phoneNum;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String hashedPassword;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String normalizedName;

    @Nullable
    private String avtUrl;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Nullable
    private String staffOfStoreCode;

    @Column(nullable = false)
    private Instant createdAt;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "jsonb")
    private List<Address> addresses;

    @Getter
    @Setter
    public static class Address {
        private String type;
        private String city;
        private String detail;
    }
}
