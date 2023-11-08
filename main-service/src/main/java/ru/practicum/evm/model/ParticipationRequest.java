package ru.practicum.evm.model;

import lombok.*;
import ru.practicum.evm.model.Event;
import ru.practicum.evm.model.User;
import ru.practicum.evm.managers.State;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "requests", schema = "public")
public class ParticipationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User requester;

    @ManyToOne
    private Event event;

    private LocalDateTime created;

    @Enumerated(EnumType.STRING)
    private State status;
}
