package ru.practicum.ewm.event.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Integer> {

    Page<Event> findAllByInitiator(Integer initiator, Pageable pageable);

    @Query(" select e from Event e "
            + "where e.initiator = ?1 "
            + "and e.state = ?2 "
            + "and (e.eventDate >= ?3 and e.eventDate <= ?4)")
    Page<Event> findForSubscriber(Integer initiator, EventState state, LocalDateTime start, LocalDateTime end,
                                  Pageable pageable);

    @Query(" select e from Event e "
            + "where e.initiator IN (?1) "
            + "and e.state IN (?2) "
            + "and e.category IN (?3) "
            + "and (e.eventDate >= ?4 and e.eventDate <= ?5)")
    Page<Event> findByAdmin(List<Integer> users, List<EventState> states, List<Integer> categories,
                            LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);

    @Query(" select e from Event e "
            + "where e.state IN (?1) "
            + "and e.category IN (?2) "
            + "and (e.eventDate >= ?3 and e.eventDate <= ?4)")
    Page<Event> findByAdminWithAllUsers(List<EventState> states, List<Integer> categories,
                               LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);

    @Query(" select e from Event e "
            + "where e.initiator IN (?1) "
            + "and e.state IN (?2) "
            + "and (e.eventDate >= ?3 and e.eventDate <= ?4)")
    Page<Event> findByAdminWithAllCategories(List<Integer> users, List<EventState> states,
                                        LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);

    @Query(" select e from Event e "
            + "where e.state IN (?1) "
            + "and (e.eventDate >= ?2 and e.eventDate <= ?3)")
    Page<Event> findByAdminWithAllUsersAndCategories(List<EventState> states,
                                             LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);

    @Query(" select e from Event e "
            + "where ( ?1 is null or upper(e.annotation) like upper(concat('%', ?1, '%')) "
            + "or upper(e.description) like upper(concat('%', ?1, '%')) ) "
            + "and e.category IN (?2) "
            + "and (?3 is null or e.paid = ?3) "
            + "and (e.eventDate >= ?4 and e.eventDate <= ?5) "
            + "and (e.participantLimit = 0 or e.participantLimit >= e.confirmedRequests) "
            + "and (e.state = 'PUBLISHED') ")
    Page<Event> findAvailableEvents(String text, List<Integer> categories, Boolean paid, LocalDateTime rangeStart,
                           LocalDateTime rangeEnd, Pageable pageRequest);

    @Query(" select e from Event e "
            + "where ( ?1 is null or upper(e.annotation) like upper(concat('%', ?1, '%')) "
            + "or upper(e.description) like upper(concat('%', ?1, '%')) ) "
            + "and e.category IN (?2) "
            + "and (?3 is null or e.paid = ?3) "
            + "and (e.eventDate >= ?4 and e.eventDate <= ?5) "
            + "and (e.state = 'PUBLISHED') ")
    Page<Event> findEvents(String text, List<Integer> categories, Boolean paid, LocalDateTime rangeStart,
                                    LocalDateTime rangeEnd, Pageable pageRequest);

    @Query(" select e from Event e "
            + "where ( ?1 is null or upper(e.annotation) like upper(concat('%', ?1, '%')) "
            + "or upper(e.description) like upper(concat('%', ?1, '%')) ) "
            + "and (?2 is null or e.paid = ?2) "
            + "and (e.eventDate >= ?3 and e.eventDate <= ?4) "
            + "and (e.participantLimit = 0 or e.participantLimit >= e.confirmedRequests) "
            + "and (e.state = 'PUBLISHED') ")
    Page<Event> findAvailableEventsAllCategories(String text, Boolean paid, LocalDateTime rangeStart,
                                    LocalDateTime rangeEnd, Pageable pageRequest);

    @Query(" select e from Event e "
            + "where ( ?1 is null or upper(e.annotation) like upper(concat('%', ?1, '%')) "
            + "or upper(e.description) like upper(concat('%', ?1, '%')) ) "
            + "and (?2 is null or e.paid = ?2) "
            + "and (e.eventDate >= ?3 and e.eventDate <= ?4) "
            + "and (e.state = 'PUBLISHED') ")
    Page<Event> findEventsAllCategories(String text, Boolean paid, LocalDateTime rangeStart,
                           LocalDateTime rangeEnd, Pageable pageRequest);

    @Query(" select e from Event e "
            + "where e.category = ?1 "
            + "and e.state IN (?2) ")
    List<Event> findByCategoryAndStates(Integer catId, List<EventState> states);

}
