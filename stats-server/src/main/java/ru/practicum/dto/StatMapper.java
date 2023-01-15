package ru.practicum.dto;

import ru.practicum.model.Stat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StatMapper {
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static Stat mapToStat(EndpointHit hit) {
        return new Stat(
                hit.getApp(),
                hit.getUri(),
                hit.getIp(),
                LocalDateTime.parse(hit.getTimestamp(), DateTimeFormatter.ofPattern(DATE_TIME_PATTERN))
        );
    }
}
