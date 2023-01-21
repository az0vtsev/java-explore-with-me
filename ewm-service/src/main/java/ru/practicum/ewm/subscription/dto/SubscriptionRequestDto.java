package ru.practicum.ewm.subscription.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import ru.practicum.ewm.subscription.model.SubscriptionRequestStatus;
import ru.practicum.ewm.user.dto.UserShortDto;

@Data
@AllArgsConstructor
public class SubscriptionRequestDto {
    @NonNull
    private Integer id;
    @NonNull
    private UserShortDto fromUser;
    @NonNull
    private UserShortDto toUser;
    @NonNull
    private SubscriptionRequestStatus status;
}
