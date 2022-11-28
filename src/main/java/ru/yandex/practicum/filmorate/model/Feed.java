package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@Builder
public class Feed {
    private long eventId;
    private long timestamp;
    private int userId;
    private String eventType;
    private String operation;
    private long entityId;
}
