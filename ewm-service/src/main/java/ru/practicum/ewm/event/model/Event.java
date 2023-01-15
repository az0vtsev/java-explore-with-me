package ru.practicum.ewm.event.model;

import lombok.*;
import ru.practicum.ewm.compilation.model.Compilation;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "events")
public class Event {
    @NonNull
    private String annotation;
    @NonNull
    private Integer category;
    @Column(name = "confirmed_requests")
    private Integer confirmedRequests = 0;
    @NonNull
    @Column(name = "created_on")
    private LocalDateTime createdOn;
    @NonNull
    private String description;
    @NonNull
    @Column(name = "event_date")
    private LocalDateTime eventDate;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NonNull
    private Integer initiator;
    @NonNull
    private Float lat;
    @NonNull
    private Float lon;
    @NonNull
    private Boolean paid;
    @NonNull
    @Column(name = "participant_limit")
    private Integer participantLimit;
    @Column(name = "published_on")
    private LocalDateTime publishedOn;
    @NonNull
    @Column(name = "request_moderation")
    private Boolean requestModeration;
    @NonNull
    @Enumerated(EnumType.STRING)
    private EventState state;
    @NonNull
    private String title;
    private Integer views = 0;
    @ManyToMany(mappedBy = "events")
    private Set<Compilation> compilations;
}
