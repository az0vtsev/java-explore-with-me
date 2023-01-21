package ru.practicum.ewm.subscription.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.subscription.dto.SubscriptionRequestDto;
import ru.practicum.ewm.subscription.service.SubscriptionService;
import ru.practicum.ewm.user.dto.UserShortDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@Validated
@RequestMapping("/users")
public class PrivateSubscriptionController {
    private final SubscriptionService service;

    @Autowired
    public PrivateSubscriptionController(SubscriptionService service) {
        this.service = service;
    }

    @GetMapping("/{userId}/subscribers")
    public List<UserShortDto> getUserSubscribers(@Positive @PathVariable Integer userId,
                                                 @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                                 @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("GET /users/{}/subscribers?from={}&size={} request received", userId, from, size);
        return service.getUserSubscribers(userId, from, size);
    }

    @GetMapping("/{userId}/subscriptions")
    public List<UserShortDto> getUserSubscriptions(@Positive @PathVariable Integer userId,
                                                   @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                                   @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("GET /users/{}/subscriptions?from={}&size={} request received", userId, from, size);
        return service.getUserSubscriptions(userId, from, size);
    }

    @GetMapping("/{userId}/subscribers/request")
    public List<SubscriptionRequestDto> getUserRequestSubscriptions(@Positive @PathVariable Integer userId) {
        log.info("GET /users/{}/subscriptions/request request received", userId);
        return service.getUserRequestSubscriptions(userId);
    }

    @PostMapping("/{subscriberId}/subscribe/{publisherId}")
    public void subscribeToUser(@Positive @PathVariable Integer subscriberId,
                                @Positive @PathVariable Integer publisherId) {
        log.info("POST /users/{}/subscribe/{} request received", subscriberId, publisherId);
        service.subscribeToUser(subscriberId, publisherId);
    }

    @PatchMapping("/{userId}/subscribers/request/{requestId}/confirm")
    public void confirmSubscribe(@Positive @PathVariable Integer userId,
                                @Positive @PathVariable Integer requestId) {
        log.info("PATCH /users/{}/subscribers/request/{}/confirm", userId, requestId);
        service.updateSubscribeStatus(userId, requestId, true);
    }

    @PatchMapping("/{userId}/subscribers/request/{requestId}/reject")
    public void rejectSubscribe(@Positive @PathVariable Integer userId,
                                 @Positive @PathVariable Integer requestId) {
        log.info("PATCH /users/{}/subscribers/request/{}/reject", userId, requestId);
        service.updateSubscribeStatus(userId, requestId, false);
    }

    @DeleteMapping("/{subscriberId}/subscribe/{publisherId}")
    public void unsubscribeFromUser(@Positive @PathVariable Integer subscriberId,
                                    @Positive @PathVariable Integer publisherId) {
        log.info("DELETE /users/{}/subscribe/{} request received", subscriberId, publisherId);
        service.unsubscribeFromUser(subscriberId, publisherId);
    }

}
