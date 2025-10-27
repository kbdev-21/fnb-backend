package com.example.fnb.customer.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateCustomerDto {
    @NotBlank
    @Pattern(regexp = "^\\+?[0-9]{1,3}?[- ]?[0-9]{3,4}[- ]?[0-9]{3,4}$")
    private String phoneNum;

    @Email
    @Nullable
    private String email;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Nullable
    private UUID userId;
}
