package ru.practicum.main.request.dto;

import lombok.*;
import ru.practicum.main.request.Status;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestShortDto {
    private List<Long> requestIds;
    private Status status;
}
