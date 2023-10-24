package ru.practicum.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RequestStat {
    private String app;
    private String uri;
    private Integer hits;
}
