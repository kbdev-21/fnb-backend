package com.example.fnb.store.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateStoreDto {

    @Size(max = 100)
    private String displayName;

    @Pattern(regexp = "^\\+?[0-9]{1,3}?[- ]?[0-9]{3,4}[- ]?[0-9]{3,4}$")
    private String phoneNum;

    @Email
    private String email;

    @Size(max = 50)
    private String city;

    @Size(max = 200)
    private String fullAddress;
}