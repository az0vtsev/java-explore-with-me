package ru.practicum.ewm.compilation.service;

import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;

import java.util.List;

public interface CompilationService {
    CompilationDto createCompilation(NewCompilationDto compilationDto);

    void deleteCompilation(int compId);

    void deleteEvent(int compId, int eventId);

    void addEvent(int compId, int eventId);

    void pinCompilation(int compId);

    void unpinCompilation(int compId);

    List<CompilationDto> getCompilations(boolean pinned, int from, int size);

    CompilationDto getCompilation(int compId);
}
