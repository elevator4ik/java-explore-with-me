package ru.practicum.evm.managers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.evm.model.Event;
import ru.practicum.evm.repository.EventRepository;
import ru.practicum.evm.exception.BadRequestException;
import ru.practicum.evm.exception.IncorrectObjectException;
import ru.practicum.evm.exception.ForbiddenException;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class EventManager {

    private final EventRepository eventRepository;

    public void isEventDateBeforeTwoHours(LocalDateTime date) throws BadRequestException {
        if (date.isBefore(LocalDateTime.now().plusHours(2))) {
            throw new BadRequestException("Incorrect time of event");
        }
    }

    public void eventExist(Long eventId) throws IncorrectObjectException {
        if (!eventRepository.existsById(eventId)) {
            throw new IncorrectObjectException("There is no event with id " + eventId);
        }
    }

    public void eventInitiatorIsNot(Long eventId, Long userId) throws BadRequestException {
        if (!Objects.equals(eventRepository.getById(eventId).getInitiator().getId(), userId)) {
            throw new BadRequestException("User id = " + userId + " is not initiator of event id = " + eventId);
        }
    }

    public void eventInitiator(Long eventId, Long userId) throws BadRequestException {
        if (Objects.equals(eventRepository.getById(eventId).getInitiator().getId(), userId)) {
            throw new BadRequestException("User id = " + userId + " is not initiator of event id = " + eventId);
        }
    }

    public void eventPublishedState(Long eventId) throws ForbiddenException {
        Event event = eventRepository.getById(eventId);
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new ForbiddenException("It is impossible to create a request to not PUBLISHED event");
        }
    }

    public void eventPendingState(Long eventId) throws ForbiddenException {
        Event event = eventRepository.getById(eventId);
        State state = event.getState();
        if (!state.equals(State.PENDING)) {
            throw new ForbiddenException("Event in state " + state + " cannot be changed");
        }
    }

    public void checkEventLimit(Long eventId) throws ForbiddenException {
        Event event = eventRepository.getById(eventId);
        int spareParticipantSlots = event.getParticipantLimit() - event.getConfirmedRequests();
        if (spareParticipantSlots == 0) {
            throw new ForbiddenException("Participants limit for this event has been reached");
        }
    }

    public void checkNullLimitOrNotPreModeration(Long eventId) throws ForbiddenException {
        Event event = eventRepository.getById(eventId);
        if (event.getParticipantLimit() == 0 || !event.getRequestModeration()) {
            throw new ForbiddenException("No CONFIRM for this request is needed");
        }
    }

    public void checkCorrectParams(Integer from, Integer size) {
        if (from < 0) {
            throw new IllegalArgumentException("From parameter cannot be less zero");
        }
        if (size <= 0) {
            throw new IllegalArgumentException("Size parameter cannot be less or equal zero");
        }
    }
}
