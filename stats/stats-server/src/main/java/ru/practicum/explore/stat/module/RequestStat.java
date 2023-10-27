package ru.practicum.explore.stat.module;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RequestStat {
    private String app;
    private String uri;
    private Long hit;
}
