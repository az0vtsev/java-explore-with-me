package ru.practicum.ewm.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import ru.practicum.ewm.request.model.ParticipationRequestState;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ParticipationRequestDto {
    @NonNull
    private LocalDateTime created;
    @NonNull
    private Integer event;
    @NonNull
    private Integer id;
    @NonNull
    private Integer requester;
    @NonNull
    private ParticipationRequestState status;
}
