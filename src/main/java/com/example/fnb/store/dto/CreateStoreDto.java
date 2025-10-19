package com.example.fnb.store.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateStoreDto {

    @NotBlank
    @Size(max = 50)
    private String code;

    @NotBlank
    @Size(max = 100)
    private String displayName;

    @NotBlank
    @Pattern(regexp = "^\\+?[0-9]{1,3}?[- ]?[0-9]{3,4}[- ]?[0-9]{3,4}$")
    private String phoneNum;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(max = 50)
    private String city;

    @NotBlank
    @Size(max = 200)
    private String fullAddress;

    @NotNull
    @Valid
    private List<StoreCreateDtoTable> tables;

    @NotNull
    private boolean open;

    @Getter
    @Setter
    public static class StoreCreateDtoTable {
        @NotBlank
        @Size(max = 50)
        private String code;

        @NotBlank
        @Size(max = 100)
        private String displayName;

        @NotBlank
        @Size(max = 200)
        private String description;
    }
}
