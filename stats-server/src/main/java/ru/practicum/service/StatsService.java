package ru.practicum.service;

import ru.practicum.dto.EndpointHit;
import ru.practicum.dto.ViewStats;

import java.util.List;

public interface StatsService {
    void save(EndpointHit hit);

    List<ViewStats> getViewStats(String start, String end, List<String> uris, Boolean unique);
}
