package com.example.fnb.store.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class TableDto {
    private UUID id;
    private String code;
    private String displayName;
    private String description;
    private TableDtoStore store;
    private Instant createdAt;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class TableDtoStore {
        private UUID id;
        private String code;
        private String displayName;
        private String phoneNum;
        private String email;
        private String city;
        private String fullAddress;
        private boolean open;
        private Instant createdAt;
    }
}
