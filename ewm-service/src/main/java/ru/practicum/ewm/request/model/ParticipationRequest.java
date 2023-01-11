package ru.practicum.ewm.request.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "requests")
public class ParticipationRequest {
    @NonNull
    private LocalDateTime created;
    @NonNull
    private Integer event;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NonNull
    private Integer requester;
    @NonNull
    @Enumerated(EnumType.STRING)
    private ParticipationRequestState state;
}
