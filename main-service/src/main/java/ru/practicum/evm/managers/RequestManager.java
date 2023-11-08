package ru.practicum.evm.managers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.evm.exception.BadRequestException;
import ru.practicum.evm.exception.IncorrectObjectException;
import ru.practicum.evm.exception.ForbiddenException;
import ru.practicum.evm.model.ParticipationRequest;
import ru.practicum.evm.repository.ParticipationRepository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestManager {

    private final ParticipationRepository repository;

    public void pending(Long reqId) throws ForbiddenException {
        final ParticipationRequest request = repository.getById(reqId);
        if (!request.getStatus().equals(State.PENDING)) {
            throw new ForbiddenException("Only request in status PENDING can be rejected");
        }
    }

    public void reConfirmed(Long reqId) throws ForbiddenException {
        final ParticipationRequest request = repository.getById(reqId);
        if (request.getStatus().equals(State.CONFIRMED)) {
            throw new ForbiddenException("Request in already in status CONFIRMED");
        }
    }

    public void correctEventRequest(Long eventId, Long reqId) throws BadRequestException {
        final ParticipationRequest request = repository.getById(reqId);
        if (!Objects.equals(request.getEvent().getId(), eventId)) {
            throw new BadRequestException("Incorrect event request");
        }
    }

    public void requestAlreadyExist(Long userId, Long eventId) {
        if (repository.existsByRequesterIdAndEventId(userId, eventId)) {
            throw new IllegalArgumentException("Request already exist");
        }
    }

    public void requester(Long userId, Long requestId) throws BadRequestException {
        ParticipationRequest request = repository.getById(requestId);
        if (!Objects.equals(request.getRequester().getId(), userId)) {
            throw new BadRequestException("Requester exception");
        }
    }

    public void canceled(Long requestId) throws ForbiddenException {
        ParticipationRequest request = repository.getById(requestId);
        if (request.getStatus().equals(State.CANCELED)) {
            throw new ForbiddenException("Request in already in status CANCELED");
        }
    }

    public void requestExists(Long requestId) throws IncorrectObjectException {
        if (!repository.findAll().isEmpty()) {
            List<Long> ids = repository.findAll()
                    .stream()
                    .map(ParticipationRequest::getId)
                    .collect(Collectors.toList());
            if (!ids.contains(requestId)) {
                throw new IncorrectObjectException("There is no request with id = " + requestId);
            }
        } else {
            throw new IncorrectObjectException("There is no request with id = " + requestId);
        }
    }
}
