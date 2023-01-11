package ru.practicum.ewm.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class LocationDto {
    @NonNull
    private Float lat;
    @NonNull
    private Float lon;
}
