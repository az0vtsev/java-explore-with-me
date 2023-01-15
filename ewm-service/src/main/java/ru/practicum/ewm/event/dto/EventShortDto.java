package ru.practicum.ewm.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.user.dto.UserShortDto;

@Data
@AllArgsConstructor
public class EventShortDto {
    @NonNull
    private String annotation;
    @NonNull
    private CategoryDto category;
    @NonNull
    private Integer confirmedRequests;
    @NonNull
    private String eventDate;
    @NonNull
    private Integer id;
    @NonNull
    private UserShortDto initiator;
    @NonNull
    private Boolean paid;
    @NonNull
    private String title;
    @NonNull
    private Integer views;
}
