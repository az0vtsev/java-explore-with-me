package ru.practicum.ewm.subscription.dto;

import ru.practicum.ewm.subscription.model.SubscriptionRequest;
import ru.practicum.ewm.user.dto.UserShortDto;

public class SubscriptionRequestMapper {
    public static SubscriptionRequestDto mapToSubscriptionRequestDto(SubscriptionRequest request,
                                                                     UserShortDto fromUser, UserShortDto toUser) {
        return new SubscriptionRequestDto(
                request.getId(),
                fromUser,
                toUser,
                request.getStatus()
        );
    }
}
