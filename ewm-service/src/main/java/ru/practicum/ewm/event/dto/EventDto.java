package ru.practicum.ewm.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.user.dto.UserShortDto;

@Data
@AllArgsConstructor
public class EventDto {
    @NonNull
    private String annotation;
    @NonNull
    private CategoryDto category;
    private Integer confirmedRequests;
    private String createdOn;
    private String description;
    @NonNull
    private String eventDate;
    private Integer id;
    @NonNull
    private UserShortDto initiator;
    @NonNull
    private LocationDto location;
    @NonNull
    private Boolean paid;
    private Integer participantLimit;
    private String publishedOn;
    private Boolean requestModeration;
    private EventState state;
    @NonNull
    private String title;
    private Integer views;
}
