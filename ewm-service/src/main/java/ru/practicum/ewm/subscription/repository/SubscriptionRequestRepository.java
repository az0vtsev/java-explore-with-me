package ru.practicum.ewm.subscription.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.subscription.model.SubscriptionRequest;
import ru.practicum.ewm.subscription.model.SubscriptionRequestStatus;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRequestRepository extends JpaRepository<SubscriptionRequest, Integer> {
    Optional<SubscriptionRequest> findByFromUserAndToUser(Integer fromUser, Integer toUser);

    Optional<SubscriptionRequest> findByFromUserAndToUserAndStatus(Integer fromUser, Integer toUser,
                                                                   SubscriptionRequestStatus status);

    Page<SubscriptionRequest> findAllByToUserAndStatus(Integer toUser, SubscriptionRequestStatus status,
                                                       Pageable pageable);

    List<SubscriptionRequest> findAllByToUserAndStatus(Integer fromUser, SubscriptionRequestStatus status);

    Page<SubscriptionRequest> findAllByFromUserAndStatus(Integer fromUser, SubscriptionRequestStatus status,
                                                         Pageable pageable);
}
