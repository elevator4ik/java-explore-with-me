package ru.practicum.evm.mapper;

import org.springframework.stereotype.Service;
import ru.practicum.evm.dto.request.ParticipationRequestDto;
import ru.practicum.evm.model.ParticipationRequest;

import java.util.ArrayList;
import java.util.List;

@Service
public class ParticipationMapper {

    public ParticipationRequestDto toDto(ParticipationRequest request) {
        return new ParticipationRequestDto(request.getId(),
                request.getRequester().getId(),
                request.getEvent().getId(),
                request.getCreated(),
                request.getStatus().toString());
    }

    public List<ParticipationRequestDto> toDtosList(Iterable<ParticipationRequest> requests) {
        List<ParticipationRequestDto> participationRequestDtos = new ArrayList<>();
        for (ParticipationRequest request : requests) {
            participationRequestDtos.add(toDto(request));
        }
        return participationRequestDtos;
    }
}
