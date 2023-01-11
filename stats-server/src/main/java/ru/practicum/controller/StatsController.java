package ru.practicum.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EndpointHit;
import ru.practicum.dto.ViewStats;
import ru.practicum.service.StatsService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
public class StatsController {
    private final StatsService service;

    @Autowired
    public StatsController(StatsService service) {
        this.service = service;
    }

    @PostMapping("/hit")
    void saveHit(@Valid @RequestBody EndpointHit hit) {
        log.info("POST /hit request received, request body={}", hit);
        service.save(hit);
    }

    @GetMapping("/stats")
    List<ViewStats> getViewStats(@RequestParam String start,
                                 @RequestParam String end,
                                 @RequestParam(defaultValue = "0") List<String> uris,
                                 @RequestParam(defaultValue = "false") Boolean unique) {
        log.info("GET /stats?start={}&send={}&uris={}&unique={} request received", start, end, uris, unique);
        return service.getViewStats(start, end, uris, unique);
    }

}
