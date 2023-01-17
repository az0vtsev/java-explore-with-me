package ru.practicum.ewm.subscription.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.exception.NotValidDataException;
import ru.practicum.ewm.subscription.dto.SubscriptionRequestDto;
import ru.practicum.ewm.subscription.dto.SubscriptionRequestMapper;
import ru.practicum.ewm.subscription.model.SubscriptionRequest;
import ru.practicum.ewm.subscription.model.SubscriptionRequestStatus;
import ru.practicum.ewm.subscription.repository.SubscriptionRequestRepository;
import ru.practicum.ewm.user.dto.UserMapper;
import ru.practicum.ewm.user.dto.UserShortDto;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {
    private final SubscriptionRequestRepository subscriptionRepository;
    private final UserRepository userRepository;

    @Autowired
    public SubscriptionServiceImpl(SubscriptionRequestRepository subscriptionRepository, UserRepository userRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void subscribeToUser(int subscriberId, int publisherId) {
        checkUserExists(subscriberId);
        checkUserExists(publisherId);
        Optional<SubscriptionRequest> request = subscriptionRepository
                .findByFromUserAndToUser(subscriberId, publisherId);
        if (request.isPresent()) {
            if (request.get().getStatus() == SubscriptionRequestStatus.CONFIRM) {
                throw new NotValidDataException("User id=" + subscriberId + " already subscribe to user id="
                        + publisherId + ".");
            }
            if (request.get().getStatus() == SubscriptionRequestStatus.WAITING) {
                throw new NotValidDataException("User id=" + subscriberId + " already have subscribe request" +
                        " to user id=" + publisherId + ".");
            }
        }
        subscriptionRepository.save(new SubscriptionRequest(
                subscriberId,
                publisherId,
                SubscriptionRequestStatus.WAITING,
                LocalDateTime.now()));
    }

    @Override
    public void unsubscribeFromUser(int subscriberId, int publisherId) {
        checkUserExists(subscriberId);
        checkUserExists(publisherId);
        Optional<SubscriptionRequest> request = subscriptionRepository
                .findByFromUserAndToUserAndStatus(subscriberId, publisherId, SubscriptionRequestStatus.CONFIRM);
        if (request.isEmpty()) {
            throw new NotValidDataException("User id=" + subscriberId + " doesn't subscribe to user id="
                    + publisherId + ".");
        }
        subscriptionRepository.deleteById(request.get().getId());
    }

    @Override
    public List<UserShortDto> getUserSubscribers(int userId, int from, int size) {
        checkUserExists(userId);
        List<SubscriptionRequest> subscribeRequests = subscriptionRepository
                .findAllByToUserAndStatus(userId, SubscriptionRequestStatus.CONFIRM,
                        getPageRequest(from, size)).stream().collect(Collectors.toList());
        List<UserShortDto> subscribers = new ArrayList<>();
        subscribeRequests.forEach(request -> {
            User user = userRepository.findById(request.getFromUser())
                    .orElseThrow(() -> new NotFoundException("User id=" + userId + " was not found"));
            subscribers.add(UserMapper.mapToUserShortDto(user));
        });
        return subscribers;
    }

    @Override
    public List<UserShortDto> getUserSubscriptions(int userId,  int from, int size) {
        checkUserExists(userId);
        List<SubscriptionRequest> subscriptionRequests = subscriptionRepository
                .findAllByFromUserAndStatus(userId, SubscriptionRequestStatus.CONFIRM,
                        getPageRequest(from, size)).stream().collect(Collectors.toList());
        List<UserShortDto> subscriptions = new ArrayList<>();
        subscriptionRequests.forEach(request -> {
            User user = userRepository.findById(request.getToUser())
                    .orElseThrow(() -> new NotFoundException("User id=" + userId + " was not found"));
            subscriptions.add(UserMapper.mapToUserShortDto(user));
        });
        return subscriptions;
    }

    @Override
    public List<SubscriptionRequestDto> getUserRequestSubscriptions(int userId) {
        checkUserExists(userId);
        List<SubscriptionRequest> requests = subscriptionRepository
                .findAllByToUserAndStatus(userId, SubscriptionRequestStatus.WAITING);
        List<SubscriptionRequestDto> requestDtos = new ArrayList<>();
        requests.forEach(request -> {
            User userFrom = userRepository.findById(request.getFromUser())
                    .orElseThrow(() -> new NotFoundException("Subscription request id=" + request.getFromUser()
                            + " was not found"));
            User userTo = userRepository.findById(request.getToUser())
                    .orElseThrow(() -> new NotFoundException("Subscription request id=" + request.getFromUser()
                            + " was not found"));
            UserShortDto userFromDto = UserMapper.mapToUserShortDto(userFrom);
            UserShortDto userToDto = UserMapper.mapToUserShortDto(userTo);
            requestDtos.add(SubscriptionRequestMapper.mapToSubscriptionRequestDto(request, userFromDto, userToDto));
        });
        return requestDtos;
    }

    @Override
    public void updateSubscribeStatus(int userId, int requestId, boolean isConfirm) {
        checkUserExists(userId);
        SubscriptionRequest request = subscriptionRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Subscription request id=" + requestId
                        + " was not found"));
        if (request.getToUser() != userId) {
            throw new NotValidDataException("User id=" + userId + " doesn't have access to request id="
                    + requestId + ".");
        }
        if (request.getStatus() != SubscriptionRequestStatus.WAITING) {
            throw new NotValidDataException("Request id=" + requestId + " can't be updated, already have status ="
                    + request.getStatus() + ".");
        }
        if (isConfirm) {
            request.setStatus(SubscriptionRequestStatus.CONFIRM);
            subscriptionRepository.save(request);
        } else {
            request.setStatus(SubscriptionRequestStatus.REJECT);
            subscriptionRepository.save(request);
        }
    }

    private void checkUserExists(int userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User id=" + userId + " was not found");
        }
    }

    private PageRequest getPageRequest(int from, int size) {
        int page = from / size;
        return PageRequest.of(page, size);
    }
}
