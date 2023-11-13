package ru.practicum.main.request.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmedRequestShortDto {
    private Long eventId;
    private Long confirmedRequestsCount;
}
