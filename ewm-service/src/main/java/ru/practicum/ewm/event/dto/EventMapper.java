package ru.practicum.ewm.event.dto;

import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.exception.NotValidDataException;
import ru.practicum.ewm.user.dto.UserShortDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EventMapper {

    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static Event mapToEvent(NewEventDto newEventDto, int userId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime eventDate = LocalDateTime.parse(newEventDto.getEventDate(),
                DateTimeFormatter.ofPattern(DATE_TIME_PATTERN));
        if (now.plusHours(2).isAfter(eventDate)) {
            throw new NotValidDataException("Event date and time must be 2 hours later from now");
        }
        return new Event(
                newEventDto.getAnnotation(),
                newEventDto.getCategory(),
                now,
                newEventDto.getDescription(),
                eventDate,
                userId,
                newEventDto.getLocation().getLat(),
                newEventDto.getLocation().getLon(),
                newEventDto.getPaid() != null && newEventDto.getPaid(),
                newEventDto.getParticipantLimit() == null ? 0 : newEventDto.getParticipantLimit(),
                newEventDto.getRequestModeration() == null || newEventDto.getRequestModeration(),
                EventState.PENDING,
                newEventDto.getTitle()
        );
    }

    public static Event mapToEvent(UserUpdateEventDto updateEventDto, Event oldEvent, int userId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime eventDate;
        if (updateEventDto.getEventDate() != null) {
            eventDate = LocalDateTime.parse(updateEventDto.getEventDate(),
                    DateTimeFormatter.ofPattern(DATE_TIME_PATTERN));
        } else {
            eventDate = oldEvent.getEventDate();
        }
        if (now.plusHours(2).isAfter(eventDate)) {
            throw new NotValidDataException("Event date and time must be 2 hours later from now");
        }
        return new Event(
                updateEventDto.getAnnotation() == null ? oldEvent.getAnnotation() : updateEventDto.getAnnotation(),
                updateEventDto.getCategory() == null ? oldEvent.getCategory() : updateEventDto.getCategory(),
                now,
                updateEventDto.getDescription() == null ? oldEvent.getDescription() : updateEventDto.getDescription(),
                eventDate,
                userId,
                oldEvent.getLat(),
                oldEvent.getLon(),
                updateEventDto.getPaid() == null ? oldEvent.getPaid() : updateEventDto.getPaid(),
                updateEventDto.getParticipantLimit() == null ?
                        oldEvent.getParticipantLimit() : updateEventDto.getParticipantLimit(),
                oldEvent.getRequestModeration(),
                EventState.PENDING,
                updateEventDto.getTitle() == null ? oldEvent.getTitle() : updateEventDto.getTitle()
        );
    }

    public static EventShortDto mapToEventShortDto(Event event, CategoryDto categoryDto, UserShortDto userShortDto) {
        return new EventShortDto(
                event.getAnnotation(),
                categoryDto,
                event.getConfirmedRequests(),
                DateTimeFormatter.ofPattern(DATE_TIME_PATTERN).format(event.getEventDate()),
                event.getId(),
                userShortDto,
                event.getPaid(),
                event.getTitle(),
                event.getViews()
                );
    }

    public static EventDto mapToEventFullDto(Event event, CategoryDto categoryDto, UserShortDto userShortDto) {
        return new EventDto(
            event.getAnnotation(),
            categoryDto,
                event.getConfirmedRequests(),
                DateTimeFormatter.ofPattern(DATE_TIME_PATTERN).format(event.getCreatedOn()),
                event.getDescription(),
                DateTimeFormatter.ofPattern(DATE_TIME_PATTERN).format(event.getEventDate()),
                event.getId(),
                userShortDto,
                new LocationDto(event.getLat(), event.getLon()),
                event.getPaid(),
                event.getParticipantLimit(),
                event.getPublishedOn() == null ?
                        null : DateTimeFormatter.ofPattern(DATE_TIME_PATTERN).format(event.getPublishedOn()),
                event.getRequestModeration(),
                event.getState(),
                event.getTitle(),
                event.getViews()
        );
    }

    public static Event mapToEvent(AdminUpdateEventDto eventDto, Event oldEvent) {
        LocalDateTime eventDate;
        if (eventDto.getEventDate() != null) {
            LocalDateTime now = LocalDateTime.now();
            eventDate = LocalDateTime.parse(eventDto.getEventDate(),
                    DateTimeFormatter.ofPattern(DATE_TIME_PATTERN));
            if (now.plusHours(2).isAfter(eventDate)) {
                throw new NotValidDataException("Event date and time must be 2 hours later from now");
            }
        } else {
            eventDate = oldEvent.getEventDate();
        }
        return new Event(
                eventDto.getAnnotation() == null ? oldEvent.getAnnotation() : eventDto.getAnnotation(),
                eventDto.getCategory() == null ? oldEvent.getCategory() : eventDto.getCategory(),
                oldEvent.getPublishedOn(),
                eventDto.getDescription() == null ? oldEvent.getDescription() : eventDto.getDescription(),
                eventDate,
                oldEvent.getInitiator(),
                oldEvent.getLat(),
                oldEvent.getLon(),
                eventDto.getPaid() == null ? oldEvent.getPaid() : eventDto.getPaid(),
                eventDto.getParticipantLimit() == null ?
                        oldEvent.getParticipantLimit() : eventDto.getParticipantLimit(),
                eventDto.getRequestModeration() == null ?
                        oldEvent.getRequestModeration() : eventDto.getRequestModeration(),
                oldEvent.getState(), //???
                eventDto.getTitle() == null ? oldEvent.getTitle() : eventDto.getTitle()
        );
    }
}
