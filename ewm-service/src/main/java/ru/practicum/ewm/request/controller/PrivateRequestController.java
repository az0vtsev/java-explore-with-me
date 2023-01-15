package ru.practicum.ewm.request.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.service.ParticipationRequestService;

import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@RestController
@Validated
@RequestMapping("/users")
public class PrivateRequestController {
    private final ParticipationRequestService service;

    @Autowired
    public PrivateRequestController(ParticipationRequestService service) {
        this.service = service;
    }

    @GetMapping("/{userId}/requests")
    public List<ParticipationRequestDto> getRequests(@PathVariable int userId) {
    log.info("GET /users/{}/requests request received", userId);
        return service.getRequests(userId);
    }

    @PostMapping("/{userId}/requests")
    public ParticipationRequestDto createRequest(@Positive @PathVariable int userId,
                                                 @Positive @RequestParam int eventId) {
    log.info("POST /users/{}/requests?eventId={} request received", userId, eventId);
        return service.createRequest(userId, eventId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(@PathVariable int userId,
                                                 @PathVariable int requestId) {
    log.info("POST /users/{}/requests/{}/cancel request received", userId, requestId);
        return service.cancelRequest(userId, requestId);
    }

}
