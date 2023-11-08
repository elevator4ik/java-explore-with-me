package ru.practicum.evm.service;

import ru.practicum.evm.exception.BadRequestException;
import ru.practicum.evm.exception.IncorrectObjectException;
import ru.practicum.evm.exception.ForbiddenException;
import ru.practicum.evm.dto.request.ParticipationRequestDto;

import java.util.List;

public interface RequestService {

    ParticipationRequestDto createRequest(Long userId, Long eventId) throws IncorrectObjectException, BadRequestException, ForbiddenException;
    ParticipationRequestDto cancelRequest(Long userId, Long requestId) throws IncorrectObjectException, BadRequestException, ForbiddenException;
    List<ParticipationRequestDto> getRequestsByRequesterId(Long userId) throws IncorrectObjectException;
    ParticipationRequestDto confirmRequestByInitiator(Long initiatorId, Long eventId, Long reqId) throws IncorrectObjectException, BadRequestException, ForbiddenException;
    ParticipationRequestDto rejectRequestByInitiator(Long userId, Long eventId, Long reqId) throws IncorrectObjectException, BadRequestException, ForbiddenException;
    List<ParticipationRequestDto> getRequestsByInitiator(Long userId, Long eventId) throws IncorrectObjectException, BadRequestException;
}
