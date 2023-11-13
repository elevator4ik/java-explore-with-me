package ru.practicum.main.event.dto;

import lombok.*;
import ru.practicum.main.request.dto.RequestDto;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestShortUpdateDto {
    private List<RequestDto> confirmedRequests = new ArrayList<>();
    private List<RequestDto> rejectedRequests = new ArrayList<>();

}
