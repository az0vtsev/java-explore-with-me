package ru.practicum.ewm.event.service;

import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;

import java.util.List;

public interface EventService {
    List<EventDto> getEventsByAdmin(List<Integer> users, List<String> states, List<Integer> categories,
                             String rangeStart, String rangeEnd, int from, int size);

    EventDto adminUpdateEvent(int eventId, AdminUpdateEventDto eventDto);

    EventDto updateEventState(int eventId, boolean isPublish);

    List<EventShortDto> getEventsByUser(int userId, int from, int size);

    EventDto userUpdateEvent(int userId, UserUpdateEventDto eventDto);

    EventDto createEvent(int userId, NewEventDto eventDto);

    EventDto getEventByUser(int userId, int eventId);

    EventDto cancelEvent(int userId, int eventId);

    List<ParticipationRequestDto> getEventRequests(int userId, int eventId);

    ParticipationRequestDto confirmEventRequest(int userId, int eventId, int reqId);

    ParticipationRequestDto rejectEventRequest(int userId, int eventId, int reqId);

    List<EventShortDto> getEventsByGuest(String text, List<Integer> categories, Boolean paid, String rangeStart,
                                         String rangeEnd, Boolean onlyAvailable, String sort, int from, int size);

    EventDto getEventByGuest(int eventId);

}
