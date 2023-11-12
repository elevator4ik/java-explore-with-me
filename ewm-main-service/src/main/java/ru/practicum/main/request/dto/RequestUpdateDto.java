package ru.practicum.main.request.dto;

import lombok.*;
import ru.practicum.main.request.model.Request;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestUpdateDto {
    private List<Request> conformedRequest = new ArrayList<>();
    private List<Request> cancelRequest = new ArrayList<>();
}
