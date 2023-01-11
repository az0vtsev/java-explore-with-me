package ru.practicum.ewm.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import ru.practicum.ewm.event.dto.EventShortDto;

import java.util.List;

@Data
@AllArgsConstructor
public class CompilationDto {
    @NonNull
    private List<EventShortDto> events;
    @NonNull
    private Integer id;
    @NonNull
    private Boolean pinned;
    @NonNull
    private String title;
}
