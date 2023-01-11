package ru.practicum.ewm.compilation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.CategoryMapper;
import ru.practicum.ewm.category.repository.CategoryRepository;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.CompilationMapper;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.compilation.repository.CompilationRepository;
import ru.practicum.ewm.event.dto.EventMapper;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.exception.NotValidDataException;
import ru.practicum.ewm.user.dto.UserMapper;
import ru.practicum.ewm.user.dto.UserShortDto;
import ru.practicum.ewm.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository repository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public CompilationServiceImpl(CompilationRepository repository, EventRepository eventRepository,
                                  UserRepository userRepository, CategoryRepository categoryRepository) {
        this.repository = repository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public CompilationDto createCompilation(NewCompilationDto compilationDto) {
        Set<Event> events = compilationDto.getEvents().isEmpty() ?
                new HashSet<>() : getEventsFromIds(compilationDto.getEvents());
        Compilation newCompilation = repository.save(CompilationMapper.mapToCompilation(compilationDto, events));
        return CompilationMapper.mapToCompilationDto(newCompilation, getEventsShortDto(events));
    }

    @Override
    public void deleteCompilation(int compId) {
        if (!repository.existsById(compId)) {
            throw new NotFoundException("Compilation id=" + compId + "was not found");
        }
        repository.deleteById(compId);
    }

    @Override
    public void deleteEvent(int compId, int eventId) {
    Compilation compilation = repository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation id=" + compId + "was not found"));
    Event  event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event id=" + eventId + "was not found"));
        if (compilation.getEvents().stream().noneMatch(compEvent -> compEvent.getId() == eventId)) {
            throw new NotValidDataException("Event id=" + eventId + " wasn't add to compilation id=" + compId);
        }
        compilation.getEvents().remove(event);
        repository.save(compilation);
    }

    @Override
    public void addEvent(int compId, int eventId) {
        Compilation compilation = repository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation id=" + compId + "was not found"));
    Event  event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event id=" + eventId + "was not found"));
        if (compilation.getEvents().stream().anyMatch(compEvent -> compEvent.getId() == eventId)) {
            throw new NotValidDataException("Event id=" + eventId + " already add to compilation id=" + compId);
        }
        compilation.getEvents().add(event);
        repository.save(compilation);
    }

    @Override
    public void pinCompilation(int compId) {
        Compilation compilation = repository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation id=" + compId + "was not found"));
        if (compilation.getPinned()) {
            throw new NotValidDataException("Compilation id=" + compId + "already pinned");
        }
        compilation.setPinned(true);
        repository.save(compilation);
    }

    @Override
    public void unpinCompilation(int compId) {
        Compilation compilation = repository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation id=" + compId + "was not found"));
        if (!compilation.getPinned()) {
            throw new NotValidDataException("Compilation id=" + compId + "already unpinned");
        }
        compilation.setPinned(false);
        repository.save(compilation);
    }

    @Override
    public List<CompilationDto> getCompilations(boolean pinned, int from, int size) {
        int page = from / size;
        PageRequest pageRequest = PageRequest.of(page, size);
        List<CompilationDto> compilationsDto = new ArrayList<>();
        repository.findAllByPinned(pinned, pageRequest)
                .stream()
                .forEach(compilation -> compilationsDto
                        .add(CompilationMapper.mapToCompilationDto(compilation,
                                getEventsShortDto(compilation.getEvents()))));
        return compilationsDto;
    }

    @Override
    public CompilationDto getCompilation(int compId) {
        Compilation compilation = repository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation id=" + compId + "was not found"));
        List<EventShortDto> eventsShortDto = getEventsShortDto(compilation.getEvents());
        return CompilationMapper.mapToCompilationDto(compilation, eventsShortDto);
    }

    private Set<Event> getEventsFromIds(List<Integer> eventsIds) {
        Set<Event> events = new HashSet<>();
        eventsIds.forEach(eventId -> {
            Event event = eventRepository.findById(eventId)
                    .orElseThrow(() -> new NotFoundException("Event id=" + eventId + "was not found"));
            events.add(event);
        });
        return events;
    }

    private List<EventShortDto> getEventsShortDto(Set<Event> events) {
        List<EventShortDto> eventsShortDto = new ArrayList<>();
        events.forEach(event -> {
            CategoryDto categoryDto = CategoryMapper
                    .mapToCategoryDto(categoryRepository.findById(event.getCategory()).get());
            UserShortDto userShortDto = UserMapper
                    .mapToUserShortDto(userRepository.findById(event.getInitiator()).get());
            eventsShortDto.add(EventMapper.mapToEventShortDto(event, categoryDto, userShortDto));
        });
        return eventsShortDto;
    }
}
