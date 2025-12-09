package com.example.fnb.analytics.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class EventDto {
    private UUID id;
    private UUID actorId;
    private String title;
    private String description;
    private Map<String, String> metadata;
    private Instant occurredAt;
}
