package ru.practicum.ewm.compilation.dto;

import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.model.Event;

import java.util.List;
import java.util.Set;

public class CompilationMapper {
    public static Compilation mapToCompilation(NewCompilationDto compilationDto, Set<Event> events) {
        return new Compilation(
                compilationDto.getPinned() != null && compilationDto.getPinned(),
                compilationDto.getTitle(),
                events
        );
    }

    public static CompilationDto mapToCompilationDto(Compilation compilation, List<EventShortDto> events) {
        return new CompilationDto(
                events,
                compilation.getId(),
                compilation.getPinned(),
                compilation.getTitle()
        );
    }
}
