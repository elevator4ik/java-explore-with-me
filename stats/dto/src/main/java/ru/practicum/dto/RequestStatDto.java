package ru.practicum.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RequestStatDto {
    private String app;
    private String uri;
    private Long hits;
}
