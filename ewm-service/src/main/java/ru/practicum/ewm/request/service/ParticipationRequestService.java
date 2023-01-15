package ru.practicum.ewm.request.service;

import ru.practicum.ewm.request.dto.ParticipationRequestDto;

import java.util.List;

public interface ParticipationRequestService {
    List<ParticipationRequestDto> getRequests(int userId);

    ParticipationRequestDto createRequest(int userId, int eventId);

    ParticipationRequestDto cancelRequest(int userId, int requestId);
}
