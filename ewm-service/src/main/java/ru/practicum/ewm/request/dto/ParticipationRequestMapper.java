package ru.practicum.ewm.request.dto;

import ru.practicum.ewm.request.model.ParticipationRequest;

public class ParticipationRequestMapper {
    public static ParticipationRequestDto mapToParticipationRequestDto(ParticipationRequest request) {
        return new ParticipationRequestDto(
                request.getCreated(),
                request.getEvent(),
                request.getId(),
                request.getRequester(),
                request.getState()
        );
    }
}
