package ru.practicum.ewm.event.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.client.EndpointHit;
import ru.practicum.ewm.event.client.EventClient;
import ru.practicum.ewm.event.dto.EventDto;
import ru.practicum.ewm.event.dto.EventMapper;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@RestController
@Validated
@RequestMapping("/events")
public class PublicEventController {
    private final EventService service;
    private final EventClient client;

    @Autowired
    public PublicEventController(EventService service, EventClient client) {
        this.service = service;
        this.client = client;
    }

    @GetMapping
    List<EventShortDto> getEvents(@RequestParam(required = false) String text,
                                  @RequestParam(defaultValue = "0") List<Integer> categories,
                                  @RequestParam(required = false) Boolean paid,
                                  @RequestParam(required = false) String rangeStart,
                                  @RequestParam(required = false) String rangeEnd,
                                  @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                  @RequestParam(defaultValue = "EVENT_DATE") String sort,
                                  @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                  @Positive @RequestParam(defaultValue = "10") int size,
                                  HttpServletRequest request) {
        log.info("GET /events?text={}&categories={}&paid={}&rangeStart={}&rangeEnd={}&onlyAvailable={}&sort={}"
                        + "&from={}&size={} request received", text, categories, paid, rangeStart, rangeEnd,
                onlyAvailable, sort, from, size);
        EndpointHit hit = new EndpointHit(
                EventClient.APP_NAME,
                request.getRequestURI(),
                request.getRemoteAddr(),
                DateTimeFormatter.ofPattern(EventMapper.DATE_TIME_PATTERN).format(LocalDateTime.now()));
        client.postHit(hit);
        return service.getEventsByGuest(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
    }

    @GetMapping("/{eventId}")
    EventDto getCategory(@Positive @PathVariable int eventId, HttpServletRequest request) {
        log.info("GET /events/{} request received", eventId);
        EndpointHit hit = new EndpointHit(
                EventClient.APP_NAME,
                request.getRequestURI(),
                request.getRemoteAddr(),
                DateTimeFormatter.ofPattern(EventMapper.DATE_TIME_PATTERN).format(LocalDateTime.now()));
        client.postHit(hit);
        return service.getEventByGuest(eventId);
    }
}
