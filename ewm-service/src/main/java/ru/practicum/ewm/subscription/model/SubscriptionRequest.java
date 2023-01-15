package ru.practicum.ewm.subscription.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "subscriptions")
public class SubscriptionRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NonNull
    @Column(name = "from_user")
    private Integer fromUser;
    @NonNull
    @Column(name = "to_user")
    private Integer toUser;
    @NonNull
    @Enumerated(EnumType.STRING)
    private SubscriptionRequestStatus status;
    @NonNull
    private LocalDateTime created;
}
