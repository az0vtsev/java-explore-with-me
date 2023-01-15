package ru.practicum.ewm.event.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.AdminUpdateEventDto;
import ru.practicum.ewm.event.dto.EventDto;
import ru.practicum.ewm.event.service.EventService;

import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@RestController
@Validated
@RequestMapping("/admin/events")
public class AdminEventController {
    private final EventService service;

    @Autowired
    public AdminEventController(EventService service) {
        this.service = service;
    }

    @GetMapping
    public List<EventDto> getEventsByAdmin(@RequestParam(defaultValue = "0") List<Integer> users,
                                    @RequestParam(defaultValue = "0") List<String> states,
                                    @RequestParam(defaultValue = "0") List<Integer> categories,
                                    @RequestParam(required = false) String rangeStart,
                                    @RequestParam(required = false) String rangeEnd,
                                    @RequestParam(defaultValue = "0") int from,
                                    @RequestParam(defaultValue = "10") int size) {
        log.info("GET /admin/events?users={}&states={}&categories={}&rangeStart={}&rangeEnd={}&from={}&size{}" +
                " request received", users, states, categories, rangeStart, rangeEnd, from, size);
        return service.getEventsByAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PutMapping("/{eventId}")
    public EventDto updateEvent(@RequestBody AdminUpdateEventDto eventDto,
                                @Positive @PathVariable int eventId) {
        log.info("PUT /admin/events/{} request received, request body = {}", eventId, eventDto);
        return service.adminUpdateEvent(eventId, eventDto);
    }

    @PatchMapping("/{eventId}/publish")
    public EventDto publishEvent(@Positive @PathVariable int eventId) {
        log.info("PATCH /admin/events/{}/publish request received", eventId);
        return service.updateEventState(eventId, true);
    }

    @PatchMapping("/{eventId}/reject")
    public EventDto rejectEvent(@Positive @PathVariable int eventId) {
        log.info("PATCH /admin/events/{}/reject request received", eventId);
        return service.updateEventState(eventId, false);
    }
}
