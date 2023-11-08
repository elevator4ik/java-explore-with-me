package ru.practicum.evm.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.evm.model.Event;
import ru.practicum.evm.repository.EventRepository;
import ru.practicum.evm.exception.BadRequestException;
import ru.practicum.evm.exception.IncorrectObjectException;
import ru.practicum.evm.exception.ForbiddenException;
import ru.practicum.evm.dto.request.ParticipationRequestDto;
import ru.practicum.evm.mapper.ParticipationMapper;
import ru.practicum.evm.model.ParticipationRequest;
import ru.practicum.evm.repository.ParticipationRepository;
import ru.practicum.evm.repository.UserRepository;
import ru.practicum.evm.managers.EventManager;
import ru.practicum.evm.managers.RequestManager;
import ru.practicum.evm.managers.State;
import ru.practicum.evm.managers.UserManager;
import ru.practicum.evm.service.RequestService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final ParticipationRepository participationRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final UserManager userManager;
    private final EventManager eventManager;
    private final RequestManager requestManager;
    private final ParticipationMapper mapper;

    @Override
    public ParticipationRequestDto createRequest(Long userId, Long eventId)
            throws IncorrectObjectException, BadRequestException, ForbiddenException {
        userManager.checkUserExists(userId);
        eventManager.eventExist(eventId);
        requestManager.requestAlreadyExist(userId, eventId);
        eventManager.eventInitiatorIsNot(eventId, userId);
        eventManager.eventPublishedState(eventId);
        eventManager.checkEventLimit(eventId);
        ParticipationRequest request = new ParticipationRequest();
        request.setRequester(userRepository.getById(userId));
        request.setEvent(eventRepository.getById(eventId));
        request.setCreated(LocalDateTime.now());
        if (eventRepository.getById(eventId).getRequestModeration()) {
            request.setStatus(State.PENDING);
        } else {
            request.setStatus(State.CONFIRMED);
        }
        return mapper.toDto(participationRepository.save(request));
    }

    @Override
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) throws IncorrectObjectException, BadRequestException, ForbiddenException {
        userManager.checkUserExists(userId);
        requestManager.requestExists(requestId);
        requestManager.requester(userId, requestId);
        requestManager.canceled(requestId);
        ParticipationRequest request = participationRepository.getById(requestId);
        request.setStatus(State.CANCELED);
        return mapper.toDto(participationRepository.save(request));
    }

    @Override
    public List<ParticipationRequestDto> getRequestsByRequesterId(Long userId) throws IncorrectObjectException {
        userManager.checkUserExists(userId);
        return mapper.toDtosList(participationRepository.findAllByRequesterId(userId));
    }

    @Override
    public ParticipationRequestDto confirmRequestByInitiator(Long userId, Long eventId, Long reqId)
            throws IncorrectObjectException, BadRequestException, ForbiddenException {
        userManager.checkUserExists(userId);
        eventManager.eventExist(eventId);
        requestManager.requestExists(reqId);
        eventManager.eventInitiator(eventId, userId);
        requestManager.correctEventRequest(eventId, reqId);
        requestManager.reConfirmed(reqId);
        ParticipationRequest request = participationRepository.getById(reqId);
        request.setStatus(State.CONFIRMED);
        Event event = eventRepository.getById(eventId);
        event.setConfirmedRequests(event.getConfirmedRequests() + 1);
        eventRepository.save(event);
        cancelOtherRequests(eventId);
        return mapper.toDto(participationRepository.save(request));
    }

    @Override
    public ParticipationRequestDto rejectRequestByInitiator(Long userId, Long eventId, Long reqId)
            throws IncorrectObjectException, BadRequestException, ForbiddenException {
        userManager.checkUserExists(userId);
        eventManager.eventExist(eventId);
        requestManager.requestExists(reqId);
        eventManager.eventInitiator(eventId, userId);
        requestManager.correctEventRequest(eventId, reqId);
        requestManager.pending(reqId);
        ParticipationRequest request = participationRepository.getById(reqId);
        request.setStatus(State.REJECTED);
        return mapper.toDto(participationRepository.save(request));
    }

    @Override
    public List<ParticipationRequestDto> getRequestsByInitiator(Long userId, Long eventId) throws IncorrectObjectException, BadRequestException {
        userManager.checkUserExists(userId);
        eventManager.eventExist(eventId);
        eventManager.eventInitiator(eventId, userId);
        return mapper.toDtosList(participationRepository.findAllByInitiator(userId, eventId));
    }

    private void cancelOtherRequests(Long eventId) {
        final Event event = eventRepository.getById(eventId);
        int spareParticipantSlots = event.getParticipantLimit() - event.getConfirmedRequests();
        if (spareParticipantSlots == 0) {
            List<ParticipationRequest> notConfirmedRequests = participationRepository
                    .findAllNotConfirmedRequestsByEventId(eventId);
            for (ParticipationRequest request : notConfirmedRequests) {
                request.setStatus(State.REJECTED);
                participationRepository.save(request);
            }
        }
    }
}
