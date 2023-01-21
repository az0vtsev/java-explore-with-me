package ru.practicum.ewm.event.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.EventDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.dto.UserUpdateEventDto;
import ru.practicum.ewm.event.service.EventService;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@Validated
@RequestMapping("/users")
public class PrivateEventController {
    private final EventService service;

    @Autowired
    public PrivateEventController(EventService service) {
        this.service = service;
    }

    @GetMapping("/{userId}/subscriptions/{publisherId}/events")
    List<EventShortDto> getUserSubscriptionEvents(@Positive @PathVariable int userId,
                                                  @Positive @PathVariable int publisherId,
                                                  @RequestParam(required = false) String rangeStart,
                                                  @RequestParam(required = false) String rangeEnd,
                                                  @PositiveOrZero @RequestParam (defaultValue = "0") int from,
                                                  @Positive @RequestParam (defaultValue = "10") int size) {
        log.info("GET /users/{}/subscriptions/{}/events?rangeStart={}&rangeEnd={}&from={}&size={} request received",
                userId, publisherId, rangeStart, rangeEnd, from, size);
        return service.getEventsByPublisher(userId, publisherId, rangeStart, rangeEnd, from, size);
    }

    @GetMapping("/{userId}/events")
    List<EventShortDto> getUserEvents(@Positive @PathVariable int userId,
                                      @PositiveOrZero @RequestParam (defaultValue = "0") int from,
                                      @Positive @RequestParam (defaultValue = "10") int size) {
        log.info("GET /users/{}/events?from={}&size={} request received", userId, from, size);
        return service.getEventsByUser(userId, from, size);
    }

    @PatchMapping("/{userId}/events")
    EventDto userUpdateEvent(@Valid @RequestBody UserUpdateEventDto eventDto,
                             @Positive @PathVariable int userId) {
        log.info("PATCH /users/{}/events request received, request body = {}", userId, eventDto);
        return service.userUpdateEvent(userId, eventDto);
    }

    @PostMapping("/{userId}/events")
    EventDto createEvent(@Valid @RequestBody NewEventDto eventDto,
                         @Positive @PathVariable int userId) {
        log.info("POST /users/{}/events request received, request body = {}", userId, eventDto);
        return service.createEvent(userId, eventDto);
    }

    @GetMapping("/{userId}/events/{eventId}")
    EventDto getUserEvent(@Positive @PathVariable int userId,
                          @Positive @PathVariable int eventId) {
        log.info("GET /users/{}/events/{} request received", userId, eventId);
        return service.getEventByUser(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    EventDto cancelEvent(@Positive @PathVariable int userId,
                         @Positive @PathVariable int eventId) {
        log.info("PATCH /users/{}/events/{} request received", userId, eventId);
        return service.cancelEvent(userId, eventId);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    List<ParticipationRequestDto> getEventRequests(@Positive @PathVariable int userId,
                                                   @Positive @PathVariable int eventId) {
        log.info("GET /users/{}/events/{}/requests request received", userId, eventId);
        return service.getEventRequests(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests/{reqId}/confirm")
    ParticipationRequestDto confirmEventRequests(@Positive @PathVariable int userId,
                                                 @Positive @PathVariable int eventId,
                                                 @Positive @PathVariable int reqId) {
        log.info("PATCH /users/{}/events/{}/requests/{}/confirm request received", userId, eventId, reqId);
        return service.confirmEventRequest(userId, eventId, reqId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests/{reqId}/reject")
    ParticipationRequestDto rejectEventRequests(@Positive @PathVariable int userId,
                                                @Positive @PathVariable int eventId,
                                                @Positive @PathVariable int reqId) {
        log.info("PATCH /users/{}/events/{}/requests/{}/reject request received", userId, eventId, reqId);
        return service.rejectEventRequest(userId, eventId, reqId);
    }

}
