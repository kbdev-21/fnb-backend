package com.example.fnb.store.dto;

import com.example.fnb.store.domain.entity.Store;
import com.example.fnb.store.domain.entity.StoreTable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class StoreDto {
    private UUID id;
    private String code;
    private String displayName;
    private String phoneNum;
    private String email;
    private String city;
    private String fullAddress;
    private List<StoreDtoTable> tables;
    private boolean open;
    private Instant createdAt;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class StoreDtoTable {
        private UUID id;
        private String code;
        private String displayName;
        private String description;
        private Instant createdAt;
    }
}
