package ru.practicum.ewm.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.request.model.ParticipationRequest;
import ru.practicum.ewm.request.model.ParticipationRequestState;

import java.util.List;

public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Integer> {

    List<ParticipationRequest> findAllByRequester(Integer requester);

    List<ParticipationRequest> findByEvent(Integer event);

    List<ParticipationRequest> findAllByEventAndState(Integer event, ParticipationRequestState state);
}
