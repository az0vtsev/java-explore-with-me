package ru.practicum.ewm.request.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.category.repository.CategoryRepository;
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
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ParticipationRequestServiceImpl implements ParticipationRequestService {
    private final ParticipationRequestRepository repository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public ParticipationRequestServiceImpl(ParticipationRequestRepository repository, EventRepository eventRepository,
                                           UserRepository userRepository, CategoryRepository categoryRepository) {
        this.repository = repository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<ParticipationRequestDto> getRequests(int userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User id=" + userId + " was not found");
        }
        List<ParticipationRequest> requests = repository.findAllByRequester(userId);
        return requests.stream()
                .map(ParticipationRequestMapper::mapToParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public ParticipationRequestDto createRequest(int userId, int eventId) {
        User requester = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User id=" + userId + " was not found"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event id=" + eventId + " was not found"));

        List<ParticipationRequest> userRequests = repository.findAllByRequester(requester.getId());
        if (userRequests.stream().anyMatch(request -> request.getEvent() == eventId)) {
            throw new NotValidDataException("User id=" + userId +
                    " already have request of event id=" + eventId);
        }
        if (event.getInitiator() == requester.getId()) {
            throw new NotValidDataException("User id=" + userId +
                    " initiator of event id=" + eventId + "and cannot request");
        }
        if (event.getState() != EventState.PUBLISHED) {
            throw new NotValidDataException("Event id=" + eventId + " must be published");
        }
        if (event.getParticipantLimit() != 0 &&
                Objects.equals(event.getConfirmedRequests(), event.getParticipantLimit())) {
            throw new NotValidDataException("Event id=" + eventId + " already have max limit requests");
        }
        ParticipationRequest newRequest = repository.save(new ParticipationRequest(LocalDateTime.now(), eventId, userId,
              event.getRequestModeration() ? ParticipationRequestState.PENDING : ParticipationRequestState.CONFIRMED));
        return ParticipationRequestMapper.mapToParticipationRequestDto(newRequest);
    }

    @Override
    public ParticipationRequestDto cancelRequest(int userId, int requestId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User id=" + userId + " was not found");
        }
        ParticipationRequest request = repository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Request id=" + requestId + " was not found"));
        if (request.getRequester() != userId) {
            throw new NotValidDataException("User id=" + userId + " isn't requestor request id=" + requestId);
        }
        request.setState(ParticipationRequestState.CANCELED);
        return ParticipationRequestMapper.mapToParticipationRequestDto(repository.save(request));
    }
}
