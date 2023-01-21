package ru.practicum.ewm.subscription.service;

import ru.practicum.ewm.subscription.dto.SubscriptionRequestDto;
import ru.practicum.ewm.user.dto.UserShortDto;

import java.util.List;

public interface SubscriptionService {
    void subscribeToUser(int subscriberId, int publisherId);

    void unsubscribeFromUser(int subscriberId, int publisherId);

    List<UserShortDto> getUserSubscribers(int userId, int from, int size);

    List<UserShortDto> getUserSubscriptions(int userId, int from, int size);

    List<SubscriptionRequestDto> getUserRequestSubscriptions(int userId);

    void updateSubscribeStatus(int userId, int requestId, boolean isConfirm);
}
