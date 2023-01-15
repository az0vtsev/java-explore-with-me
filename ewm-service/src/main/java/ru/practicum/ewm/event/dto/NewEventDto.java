package ru.practicum.ewm.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class NewEventDto {
    @NotNull
    @Size(min = 20, max = 2000,
          message = "Annotation must be between 20 and 2000 characters")
    private String annotation;
    @NotNull
    private Integer category;
    @NotNull
    @Size(min = 20, max = 7000,
            message = "Description must be between 20 and 7000 characters")
    private String description;
    @NotNull
    private String eventDate;
    @NotNull
    private LocationDto location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    @NotNull
    @Size(min = 3, max = 120,
            message = "Title must be between 3 and 120 characters")
    private String title;
}
