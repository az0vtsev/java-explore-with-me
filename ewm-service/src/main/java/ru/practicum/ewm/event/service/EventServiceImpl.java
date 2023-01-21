package ru.practicum.ewm.event.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.CategoryMapper;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.repository.CategoryRepository;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.exception.NotValidDataException;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.dto.ParticipationRequestMapper;
import ru.practicum.ewm.request.model.ParticipationRequest;
import ru.practicum.ewm.request.model.ParticipationRequestState;
import ru.practicum.ewm.request.repository.ParticipationRequestRepository;
import ru.practicum.ewm.subscription.model.SubscriptionRequestStatus;
import ru.practicum.ewm.subscription.repository.SubscriptionRequestRepository;
import ru.practicum.ewm.user.dto.UserMapper;
import ru.practicum.ewm.user.dto.UserShortDto;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;



@Service
public class EventServiceImpl implements EventService {

    private final EventRepository repository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ParticipationRequestRepository requestRepository;

    private final SubscriptionRequestRepository subscriptionRepository;

    @Autowired
    public EventServiceImpl(EventRepository repository, UserRepository userRepository,
                            CategoryRepository categoryRepository, ParticipationRequestRepository requestRepository,
                            SubscriptionRequestRepository subscriptionRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.requestRepository = requestRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    @Override
    public List<EventDto> getEventsByAdmin(List<Integer> users, List<String> statesStr, List<Integer> categories,
                                    String rangeStart, String rangeEnd, int from, int size) {
        List<Event> events;
        PageRequest pageRequest = getPageRequest(from, size);
        LocalDateTime rangeStartDate = parseRangeStart(rangeStart);
        LocalDateTime rangeEndDate = parseRangeEnd(rangeEnd);
        List<EventState> states = getEventStatesByString(statesStr);

        if (isUsersListDefault(users) && isCategoriesListDefault(categories)) {
            events = repository
                    .findByAdminWithAllUsersAndCategories(states, rangeStartDate, rangeEndDate, pageRequest)
                    .stream()
                    .collect(Collectors.toList());
        } else if (isCategoriesListDefault(categories)) {
            events = repository
                    .findByAdminWithAllCategories(users, states, rangeStartDate, rangeEndDate, pageRequest)
                    .stream()
                    .collect(Collectors.toList());
        } else if (isUsersListDefault(users)) {
            events = repository
                    .findByAdminWithAllUsers(states, categories, rangeStartDate, rangeEndDate, pageRequest)
                    .stream()
                    .collect(Collectors.toList());
        } else {
            events = repository.findByAdmin(users, states, categories, rangeStartDate, rangeEndDate, pageRequest)
                    .stream()
                    .collect(Collectors.toList());
        }
        return getFullEventsDto(events);
    }

    @Override
    public EventDto adminUpdateEvent(int eventId, AdminUpdateEventDto eventDto) {
        Event oldEvent = getEvent(eventId);
        Category updateCategory = eventDto.getCategory() != null ?
                getCategory(eventDto.getCategory()) : getCategory(oldEvent.getCategory());
        User initiator = getUser(oldEvent.getInitiator());
        CategoryDto categoryDto = CategoryMapper.mapToCategoryDto(updateCategory);
        UserShortDto initiatorDto = UserMapper.mapToUserShortDto(initiator);
        Event updateEvent = repository.save(EventMapper.mapToEvent(eventDto, oldEvent));
        return EventMapper.mapToEventFullDto(updateEvent, categoryDto, initiatorDto);
    }

    @Override
    public EventDto updateEventState(int eventId, boolean isPublish) {
        Event updateEvent;
        Event event = getEvent(eventId);
        Category category = getCategory(event.getCategory());
        User initiator = getUser(event.getInitiator());
        UserShortDto initiatorDto = UserMapper.mapToUserShortDto(initiator);
        CategoryDto categoryDto = CategoryMapper.mapToCategoryDto(category);
        if (isPublish) {
            if (event.getState() != EventState.PENDING) {
                throw new NotValidDataException("Event must have status PENDING");
            }
            LocalDateTime publishedOn = LocalDateTime.now();
            if (publishedOn.plusHours(1).isAfter(event.getEventDate())) {
                throw new NotValidDataException("Event date and time must be 1 hours later from published");
            }
            event.setPublishedOn(publishedOn);
            event.setState(EventState.PUBLISHED);
        } else {
            event.setState(EventState.CANCELED);
        }
        updateEvent = repository.save(event);
        return EventMapper.mapToEventFullDto(updateEvent, categoryDto, initiatorDto);
    }

    @Override
    public List<EventShortDto> getEventsByUser(int userId, int from, int size) {
        User initiator = getUser(userId);
        UserShortDto initiatorDto = UserMapper.mapToUserShortDto(initiator);
        PageRequest pageRequest = getPageRequest(from, size);
        List<EventShortDto> eventsDto = new ArrayList<>();
        repository.findAllByInitiator(userId, pageRequest)
                .stream()
                .forEach(event -> {
                    Category category = getCategory(event.getCategory());
                    CategoryDto categoryDto = CategoryMapper.mapToCategoryDto(category);
                    eventsDto.add(EventMapper.mapToEventShortDto(event, categoryDto, initiatorDto));
                });
        return eventsDto;
    }

    @Override
    public EventDto userUpdateEvent(int userId, UserUpdateEventDto eventDto) {
        User initiator = getUser(userId);
        UserShortDto initiatorDto = UserMapper.mapToUserShortDto(initiator);
        Event oldEvent = getEvent(eventDto.getEventId());
        Category category = eventDto.getCategory() == null ?
                getCategory(oldEvent.getCategory()) : getCategory(eventDto.getCategory());
        CategoryDto categoryDto = CategoryMapper.mapToCategoryDto(category);
        if (oldEvent.getInitiator() != userId) {
            throw new NotValidDataException("User id=" + userId + " isn't initiator event id=" + eventDto.getEventId());
        }
        if (oldEvent.getState() == EventState.PUBLISHED) {
            throw new NotValidDataException("Published event cannot be updated");
        }
        Event updateEvent = repository.save(EventMapper.mapToEvent(eventDto, oldEvent, userId));
        return EventMapper.mapToEventFullDto(updateEvent, categoryDto, initiatorDto);
    }

    @Override
    public EventDto createEvent(int userId, NewEventDto eventDto) {
        User initiator = getUser(userId);
        UserShortDto initiatorDto = UserMapper.mapToUserShortDto(initiator);
        Category category = getCategory(eventDto.getCategory());
        CategoryDto categoryDto = CategoryMapper.mapToCategoryDto(category);
        Event newEvent = repository.save(EventMapper.mapToEvent(eventDto, userId));
        return EventMapper.mapToEventFullDto(newEvent, categoryDto, initiatorDto);
    }

    @Override
    public EventDto getEventByUser(int userId, int eventId) {
        User initiator = getUser(userId);
        UserShortDto initiatorDto = UserMapper.mapToUserShortDto(initiator);
        Event event = getEvent(eventId);
        if (event.getInitiator() != userId) {
            throw new NotValidDataException("User id=" + userId + " isn't initiator event id=" + eventId);
        }
        Category category = getCategory(event.getCategory());
        CategoryDto categoryDto = CategoryMapper.mapToCategoryDto(category);
        return EventMapper.mapToEventFullDto(event, categoryDto, initiatorDto);
    }

    @Override
    public EventDto cancelEvent(int userId, int eventId) {
        User initiator = getUser(userId);
        UserShortDto initiatorDto = UserMapper.mapToUserShortDto(initiator);
        Event event = getEvent(eventId);
        if (event.getInitiator() != userId) {
            throw new NotValidDataException("User id=" + userId + " isn't initiator event id=" + eventId);
        }
        if (event.getState() != EventState.PENDING) {
                throw new NotValidDataException("Published or already canceled event cannot be canceled");
        }
        Category category = getCategory(event.getCategory());
        CategoryDto categoryDto = CategoryMapper.mapToCategoryDto(category);
        event.setState(EventState.CANCELED);
        return EventMapper.mapToEventFullDto(repository.save(event), categoryDto, initiatorDto);
    }

    @Override
    public List<ParticipationRequestDto> getEventRequests(int userId, int eventId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User id=" + userId + " was not found");
        }
        Event event = getEvent(eventId);
        if (event.getInitiator() != userId) {
            throw new NotValidDataException("User id=" + userId + " isn't initiator of event id=" + eventId);
        }
        List<ParticipationRequest> requests = requestRepository.findByEvent(eventId);
        return requests.stream()
                .map(ParticipationRequestMapper::mapToParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public ParticipationRequestDto confirmEventRequest(int userId, int eventId, int reqId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User id=" + userId + " was not found");
        }
        Event event = getEvent(eventId);
        ParticipationRequest request = getRequest(reqId);
        if (request.getEvent() != eventId) {
            throw new NotValidDataException("Event id=" + eventId + " don't in request id=" + reqId);
        }
        if (event.getParticipantLimit() != 0 && event.getParticipantLimit().equals(event.getConfirmedRequests())) {
            throw new NotValidDataException("Request id=" + reqId + " cannot be confirmed, request limit is max");
        }
        request.setState(ParticipationRequestState.CONFIRMED);
        event.setConfirmedRequests(event.getConfirmedRequests() + 1);
        if (event.getParticipantLimit().equals(event.getConfirmedRequests())) {
            requestRepository
                    .findAllByEventAndState(eventId, ParticipationRequestState.PENDING)
                    .forEach(canceledRequest -> {
                        canceledRequest.setState(ParticipationRequestState.REJECTED);
                        requestRepository.save(canceledRequest);
                    });
        }
        return ParticipationRequestMapper.mapToParticipationRequestDto(request);
    }

    @Override
    public ParticipationRequestDto rejectEventRequest(int userId, int eventId, int reqId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User id=" + userId + " was not found");
        }
        if (!repository.existsById(eventId)) {
            throw new NotFoundException("Event id=" + eventId + " was not found");
        }
        ParticipationRequest request = getRequest(reqId);
        if (request.getEvent() != eventId) {
            throw new NotValidDataException("Event id=" + eventId + " don't in request id=" + reqId);
        }
        request.setState(ParticipationRequestState.REJECTED);
        return ParticipationRequestMapper.mapToParticipationRequestDto(request);
    }

    @Override
    public List<EventShortDto> getEventsByGuest(String text, List<Integer> categories, Boolean paid,
                                                String rangeStart, String rangeEnd, Boolean onlyAvailable,
                                                String sort, int from, int size) {
        LocalDateTime rangeStartDate = parseRangeStart(rangeStart);
        LocalDateTime rangeEndDate = parseRangeEnd(rangeEnd);
        Sort orders = parseSort(sort);
        PageRequest pageRequest = getPageRequestSort(from, size, orders);
        List<Event> events;
        if (onlyAvailable) {
            if (isCategoriesListDefault(categories)) {
                events = repository
                        .findAvailableEventsAllCategories(text, paid, rangeStartDate, rangeEndDate, pageRequest)
                        .stream()
                        .collect(Collectors.toList());
            } else {
                events = repository
                        .findAvailableEvents(text, categories, paid, rangeStartDate, rangeEndDate, pageRequest)
                        .stream()
                        .collect(Collectors.toList());
            }
        } else {
            if (isCategoriesListDefault(categories)) {
                events = repository
                        .findEventsAllCategories(text, paid, rangeStartDate, rangeEndDate, pageRequest)
                        .stream()
                        .collect(Collectors.toList());
            } else {
                events = repository
                        .findEvents(text, categories, paid, rangeStartDate, rangeEndDate, pageRequest)
                        .stream()
                        .collect(Collectors.toList());
            }
        }
        List<EventShortDto> eventsShortDto = new ArrayList<>();
        events.forEach(event -> {
            User initiator = getUser(event.getInitiator());
            UserShortDto initiatorDto = UserMapper.mapToUserShortDto(initiator);
            Category category = getCategory(event.getCategory());
            CategoryDto categoryDto = CategoryMapper.mapToCategoryDto(category);
            event.setViews(event.getViews() + 1);
            repository.save(event);
            eventsShortDto.add(EventMapper.mapToEventShortDto(event, categoryDto, initiatorDto));
        });
        return eventsShortDto;
    }

    @Override
    public EventDto getEventByGuest(int eventId) {
        Event event = getEvent(eventId);
        if (event.getState() != EventState.PUBLISHED) {
            throw new NotValidDataException("Event id=" + eventId + " was not publish");
        }
        User initiator = getUser(event.getInitiator());
        UserShortDto initiatorDto = UserMapper.mapToUserShortDto(initiator);
        Category category = getCategory(event.getCategory());
        CategoryDto categoryDto = CategoryMapper.mapToCategoryDto(category);
        event.setViews(event.getViews() + 1);
        repository.save(event);
        return EventMapper.mapToEventFullDto(event, categoryDto, initiatorDto);
    }

    @Override
    public List<EventShortDto> getEventsByPublisher(int userId, int publisherId, String rangeStart, String rangeEnd,
                                                    int from, int size) {
        getUser(userId);
        User publisher = getUser(publisherId);
        subscriptionRepository.findByFromUserAndToUserAndStatus(userId,
                publisherId, SubscriptionRequestStatus.CONFIRM)
                .orElseThrow(() -> new NotValidDataException("User id=" + userId + "doesn't subscribe to user id=" +
                        publisherId + "."));
        UserShortDto initiatorDto = UserMapper.mapToUserShortDto(publisher);
        PageRequest pageRequest = getPageRequest(from, size);
        LocalDateTime start = parseRangeStart(rangeStart);
        LocalDateTime end = parseRangeEnd(rangeEnd);
        List<EventShortDto> eventsDto = new ArrayList<>();
        repository.findForSubscriber(userId, EventState.PUBLISHED, start, end, pageRequest)
                .stream()
                .forEach(event -> {
                    Category category = getCategory(event.getCategory());
                    CategoryDto categoryDto = CategoryMapper.mapToCategoryDto(category);
                    eventsDto.add(EventMapper.mapToEventShortDto(event, categoryDto, initiatorDto));
                });
        return eventsDto;
    }

    private boolean isUsersListDefault(List<Integer> users) {
        return (users.size() == 1 && users.get(0) == 0);
    }

    private boolean isCategoriesListDefault(List<Integer> categories) {
        return categories.size() == 1 && categories.get(0) == 0;
    }

    private List<EventDto> getFullEventsDto(List<Event> events) {
        List<EventDto> eventsDto = new ArrayList<>();
        events.forEach(event -> {
            User initiator = getUser(event.getInitiator());
            Category category = getCategory(event.getCategory());
            UserShortDto initiatorDto = UserMapper.mapToUserShortDto(initiator);
            CategoryDto categoryDto = CategoryMapper.mapToCategoryDto(category);
            eventsDto.add(EventMapper.mapToEventFullDto(event, categoryDto, initiatorDto));
        });
        return eventsDto;
    }

    private PageRequest getPageRequest(int from, int size) {
        int page = from / size;
        return PageRequest.of(page, size);
    }

    private PageRequest getPageRequestSort(int from, int size, Sort sort) {
        int page = from / size;
        return PageRequest.of(page, size, sort);
    }

    private LocalDateTime parseRangeStart(String rangeStart) {
        return rangeStart == null || rangeStart.isEmpty() ?
                LocalDateTime.now() :
                LocalDateTime.parse(rangeStart, DateTimeFormatter.ofPattern(EventMapper.DATE_TIME_PATTERN));
    }

    private LocalDateTime parseRangeEnd(String rangeEnd) {
        return rangeEnd == null || rangeEnd.isEmpty() ?
                LocalDateTime.now().plusYears(1000) :
                LocalDateTime.parse(rangeEnd, DateTimeFormatter.ofPattern(EventMapper.DATE_TIME_PATTERN));
    }

    private boolean isStatesListDefault(List<String> states) {
        return Objects.equals(states.get(0), "0");
    }

    private List<EventState> getEventStatesByString(List<String> statesStr) {
        return (isStatesListDefault(statesStr) || statesStr.isEmpty()) ?
                List.of(EventState.PENDING, EventState.PUBLISHED, EventState.CANCELED) :
                statesStr.stream()
                        .map(EventState::valueOf)
                        .collect(Collectors.toList());
    }

    private User getUser(int userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User id=" + userId + " was not found"));
    }

    private Category getCategory(int catId) {
        return categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Category id=" + catId + " was not found"));
    }

    private Event getEvent(int eventId) {
        return repository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event id=" + eventId + " was not found"));
    }

    private ParticipationRequest getRequest(int requestId) {
        return requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Request id=" + requestId + " was not found"));
    }

    private Sort parseSort(String sort) {
        Sort orders;
        switch (sort) {
            case "EVENT_DATE":
                orders = Sort.by(Sort.Direction.DESC, "eventDate");
                break;
            case "VIEWS":
                orders = Sort.by(Sort.Direction.DESC, "views");
                break;
            default:
                orders = Sort.by(Sort.Direction.DESC, "id");
        }
        return orders;
    }

}
