package ru.practicum.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Entity
@Table(name = "stats")
@AllArgsConstructor
@NoArgsConstructor
public class Stat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long statId;
    @Column(name = "app")
    private String app;
    @Column(name = "ip")
    private String ip;
    @Column(name = "time_stamp")
    private LocalDateTime timestamp;
    @Column(name = "uri")
    private String uri;
}