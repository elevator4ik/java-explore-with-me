package ru.practicum.main.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.category.repository.CategoryMainServiceRepository;
import ru.practicum.main.event.dto.*;
import ru.practicum.main.event.location.model.Location;
import ru.practicum.main.event.location.repository.LocationMainServiceRepository;
import ru.practicum.main.event.mapper.EventMapper;
import ru.practicum.main.event.model.Event;
import ru.practicum.main.event.repository.EventMainServiceRepository;
import ru.practicum.main.event.status.State;
import ru.practicum.main.event.status.UserEventStatus;
import ru.practicum.main.exception.BadRequestException;
import ru.practicum.main.exception.ConflictException;
import ru.practicum.main.exception.NotFoundException;
import ru.practicum.main.request.Status;
import ru.practicum.main.request.dto.RequestDto;
import ru.practicum.main.request.dto.RequestShortDto;
import ru.practicum.main.request.dto.RequestUpdateDto;
import ru.practicum.main.request.mapper.RequestMapper;
import ru.practicum.main.request.model.Request;
import ru.practicum.main.request.repository.RequestMainServiceRepository;
import ru.practicum.main.stat.Setter;
import ru.practicum.main.user.repository.UserMainServiceRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static ru.practicum.main.util.Util.createPageRequestAsc;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class EventPrivateServiceImpl implements EventPrivateService {

    private final EventMainServiceRepository repository;
    private final CategoryMainServiceRepository categoryMainServiceRepository;
    private final UserMainServiceRepository userMainServiceRepository;
    private final RequestMainServiceRepository requestMainServiceRepository;
    private final LocationMainServiceRepository locationMainServiceRepository;
    private final Setter setter;

    @Transactional
    @Override
    public EventFullDto createEvent(Long userId, EventRequestDto eventRequestDto) {
        log.info("createEvent - invoked");
        Event event = EventMapper.toEvent(eventRequestDto);
        event.setCategory(categoryMainServiceRepository.findById(event.getCategory().getId())
                .orElseThrow(() -> {
                    log.error("Category not exist");
                    return new NotFoundException("Category not found");
                }));
        event.setState(State.PENDING);
        event.setCreatedOn(LocalDateTime.now().withNano(0));
        event.setLocation(locationMainServiceRepository.save(event.getLocation()));
        event.setInitiator(userMainServiceRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not exist");
                    return new NotFoundException("User not found");
                }));

        log.info("Result: event {} created", event);
        return EventMapper.toEventFullDto(repository.save(event));
    }

    @Override
    public List<EventFullDto> readEventByUserId(Long userId, int from, int size) {
        log.info("readEventByUserId - invoked");
        if (!userMainServiceRepository.existsById(userId)) {
            log.error("User not exist");
            throw new NotFoundException("User not found");
        }
        List<Event> events = repository.findAllByInitiatorId(userId, createPageRequestAsc(from, size));

        if (events.isEmpty()) {
            return List.of();
        }

        setter.setViewAndRequests(events);

        log.info("Result: a list of events - size = {}", events.size());
        return EventMapper.toListEventFullDto(events);
    }

    @Override
    public EventFullDto readEventByUserIdAndEventId(Long userId, Long eventId) {
        log.info("readEventByUserIdAndEventId - invoked");
        Event event = repository.findByIdAndInitiatorId(eventId, userId).orElseThrow(() -> {
            log.error("Event not exist");
            return new NotFoundException("Event not found");
        });

        setter.setViewAndRequests(List.of(event));

        log.info("Result: event - {}", event);
        return EventMapper.toEventFullDto(event);
    }

    @Transactional
    @Override
    public EventFullDto updateEvent(Long userId, Long eventId, UpdateEventDto receivedDto) {
        log.info("updateEvent - invoked");
        PatchEventDto patchEventDto = EventMapper.toEventFromUpdateEvent(receivedDto);
        Event event = repository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> {
                    log.error("Event not exist");
                    return new NotFoundException("Event not found");
                });

        if (!event.getInitiator().getId().equals(userId)) {
            log.error("Unauthorized access by user");
            throw new ConflictException("You are not the owner of the event, update are not available to you");
        }

        if (event.getState().equals(State.PUBLISHED)) {
            log.error("Can't edit events with STATE.PUBLISHED");
            throw new ConflictException("Can't edit events that have already been published");
        }

        LocalDateTime eventTime = patchEventDto.getEventDate();
        if (eventTime != null) {
            if (eventTime.isBefore(LocalDateTime.now().plusHours(2))) {
                log.error("Datetime earlier then 2 hours before LocalDateTime.now()");
                throw new BadRequestException("The date and time of the event cannot be earlier then 2 hours before");
            }
            event.setEventDate(eventTime);
        }

        UserEventStatus status = patchEventDto.getStateAction();
        if (status != null) {
            if (status.equals(UserEventStatus.SEND_TO_REVIEW)) {
                event.setState(State.PENDING);
            }
            if (status.equals(UserEventStatus.CANCEL_REVIEW)) {
                event.setState(State.CANCELED);
            }
        }
        if (patchEventDto.getPaid() != null) {
            event.setPaid(patchEventDto.getPaid());
        }
        if (patchEventDto.getRequestModeration() != null) {
            event.setRequestModeration(patchEventDto.getRequestModeration());
        }
        if (patchEventDto.getAnnotation() != null && !patchEventDto.getAnnotation().isBlank()) {
            event.setAnnotation(patchEventDto.getAnnotation());
        }
        if (patchEventDto.getTitle() != null && !patchEventDto.getTitle().isBlank()) {
            event.setTitle(patchEventDto.getTitle());
        }
        if (patchEventDto.getDescription() != null && !patchEventDto.getDescription().isBlank()) {
            event.setDescription(patchEventDto.getDescription());
        }
        if (patchEventDto.getLocation() != null) {
            event.setLocation(getLocation(patchEventDto.getLocation())
                    .orElse(saveLocation(patchEventDto.getLocation())));
        }
        if (patchEventDto.getCategory() != null) {
            event.setCategory(categoryMainServiceRepository.findById(patchEventDto.getCategory())
                    .orElseThrow(() -> {
                        log.error("Category not exist");
                        return new NotFoundException("Category not found");
                    }));
        }

        setter.setViewAndRequests(List.of(event));

        log.info("Result: event - {} - ", event.getTitle());
        return EventMapper.toEventFullDto(event);
    }

    @Override
    public List<RequestDto> readRequestByUserIdAndEventId(Long userId, Long eventId) {
        log.info("readRequestByUserIdAndEventId - invoked");
        if (!repository.existsByIdAndInitiatorId(eventId, userId)) {
            log.error("Unauthorized access by user");
            throw new ConflictException("You are not the initiator of the event");
        }

        List<Request> requests = requestMainServiceRepository.findAllByEventId(eventId);
        log.info("Result: list of request size = {}", requests.size());
        return RequestMapper.toListRequestDto(requests);
    }

    @Transactional
    @Override
    public RequestShortUpdateDto updateRequestByOwner(Long userId, Long eventId, RequestShortDto requestShortDto) {
        log.info("updateRequestByOwner - invoked");
        RequestShortDto requestShort = RequestMapper.toRequestShort(requestShortDto);
        if (!userMainServiceRepository.existsById(userId)) {
            log.error("User with id = {} not exists", userId);
            throw new NotFoundException("User not found");
        }
        Event event = repository.findById(eventId).orElseThrow(() -> {
            log.error("Event with id = {} not exist", eventId);
            return new NotFoundException("Event not found");
        });

        if (!event.getInitiator().getId().equals(userId)) {
            log.error("Unauthorized access by user");
            throw new ConflictException("You are not the initiator of the event");
        }

        int confirmedRequest = setter.toConfirmedRequest(event); //количество подтвержденных реквестов

        if (event.getParticipantLimit() != 0 && confirmedRequest >= event.getParticipantLimit()) { //проверка
            log.error("The list of participants is full");
            throw new ConflictException("The list of participants is full, you cannot sign up");
        }

        RequestUpdateDto updateRequest = new RequestUpdateDto();

        List<Long> requestIds = requestShort.getRequestIds();

        for (Long requestId : requestIds) {
            Request request = requestMainServiceRepository.findById(requestId)
                    .orElseThrow(() -> {
                        log.error("Request with id = {} not exist", requestId);
                        return new NotFoundException("Request not found");
                    });
            if (confirmedRequest >= event.getParticipantLimit()) { //проверка на достижение лимита во время перебора
                requestShort.setStatus(Status.REJECTED);
            }
            if (requestShort.getStatus().equals(Status.CONFIRMED)) {
                request.setStatus(Status.CONFIRMED);
                updateRequest.getConfirmedRequest().add(request);
                confirmedRequest++;
            }
            if (requestShort.getStatus().equals(Status.REJECTED)) {
                request.setStatus(Status.REJECTED);
                updateRequest.getCancelRequest().add(request);
            }
        }
        log.info("Result: request {} - updated", updateRequest);
        return RequestMapper.toRequestShortUpdateDto(updateRequest);
    }

    private Optional<Location> getLocation(Location location) {
        log.trace("getLocation {} - invoked", location);
        return locationMainServiceRepository.findByLatAndLon(location.getLat(), location.getLon());
    }

    private Location saveLocation(Location location) {
        log.trace("saveLocation {} - invoked", location);
        return locationMainServiceRepository.save(location);
    }
}
