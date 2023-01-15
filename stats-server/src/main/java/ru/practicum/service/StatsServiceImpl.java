package ru.practicum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.dto.EndpointHit;
import ru.practicum.dto.StatMapper;
import ru.practicum.dto.ViewStats;
import ru.practicum.model.Stat;
import ru.practicum.repository.StatsRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class StatsServiceImpl implements StatsService {

    private final StatsRepository repository;

    @Autowired
    public StatsServiceImpl(StatsRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(EndpointHit hit) {
        repository.save(StatMapper.mapToStat(hit));
    }

    @Override
    public List<ViewStats> getViewStats(String start, String end, List<String> uris, Boolean unique) {
        LocalDateTime startDate = parseStart(start);
        LocalDateTime endDate = parseEnd(end);
        List<Stat> stats;
        if (isUriListDefault(uris)) {
            stats = repository.findStatsAllUri(startDate, endDate);
        } else {
            stats = repository.findStats(startDate, endDate, uris);
        }
        Set<ViewStats> viewStats = new HashSet<>();
        if (!stats.isEmpty()) {
            for (Stat stat:stats) {
                viewStats.add(new ViewStats(stat.getApp(), stat.getUri()));
            }
            if (unique) {
                for (ViewStats viewStat:viewStats) {
                    viewStat.setHits(repository.findStatsCountDistinct(startDate, endDate, viewStat.getUri()));
                }
            } else {
                for (ViewStats viewStat:viewStats) {
                    viewStat.setHits(repository.findStatsCount(startDate, endDate, viewStat.getUri()));
                }
            }
        }
        return new ArrayList<>(viewStats);
    }

    private LocalDateTime parseStart(String start) {
        return start == null || start.isEmpty() ?
                LocalDateTime.now() :
                LocalDateTime.parse(start, DateTimeFormatter.ofPattern(StatMapper.DATE_TIME_PATTERN));
    }

    private LocalDateTime parseEnd(String end) {
        return end == null || end.isEmpty() ?
                LocalDateTime.MAX :
                LocalDateTime.parse(end, DateTimeFormatter.ofPattern(StatMapper.DATE_TIME_PATTERN));
    }

    private boolean isUriListDefault(List<String> uris) {
        return uris == null || (uris.size() == 1 && uris.get(0).equals("0"));
    }
}
