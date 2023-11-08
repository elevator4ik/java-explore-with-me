package ru.practicum.evm.controller.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.evm.dto.event.EventFullDto;
import ru.practicum.evm.dto.event.NewEventDto;
import ru.practicum.evm.dto.event.UpdateEventUserRequestDto;
import ru.practicum.evm.service.EventService;
import ru.practicum.evm.exception.BadRequestException;
import ru.practicum.evm.exception.IncorrectObjectException;
import ru.practicum.evm.exception.ForbiddenException;
import ru.practicum.evm.dto.request.ParticipationRequestDto;
import ru.practicum.evm.service.RequestService;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/events")
@Validated
public class PrivateEventController {
    private final EventService eventService;
    private final RequestService requestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto addEvent(@PathVariable Long userId,
                                 @Valid @RequestBody NewEventDto newEventDto)
            throws IncorrectObjectException, BadRequestException, SQLException {
        log.info("User add event {}", newEventDto);
        return eventService.createEvent(newEventDto, userId);
    }

    @PatchMapping
    public EventFullDto updateEvent(@PathVariable Long userId,
                                    @RequestBody UpdateEventUserRequestDto requestDto)
            throws ForbiddenException, IncorrectObjectException, BadRequestException {
        log.info("User update event {}", requestDto);
        return eventService.updateEvent(requestDto, userId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto cancelEvent(@PathVariable Long userId,
                                    @PathVariable Long eventId)
            throws IncorrectObjectException, BadRequestException {
        log.info("User cancel event {}", eventId);
        return eventService.cancelEvent(userId, eventId);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEvent(@PathVariable Long userId,
                                            @PathVariable Long eventId)
            throws IncorrectObjectException, BadRequestException {
        log.info("User get event {} by initiator {}", userId, eventId);
        return eventService.getEventByInitiator(userId, eventId);
    }

    @GetMapping
    public List<EventFullDto> getEventsByInitiator(@PathVariable Long userId,
                                                   @RequestParam(defaultValue = "0") int from,
                                                   @RequestParam(defaultValue = "10") int size) throws IncorrectObjectException {
        return eventService.getEventsByInitiator(userId, from, size);
    }

    @PatchMapping("/{eventId}/requests/{reqId}/confirm")
    public ParticipationRequestDto confirmRequest(@PathVariable Long userId,
                                                  @PathVariable Long eventId,
                                                  @PathVariable Long reqId)
            throws ForbiddenException, IncorrectObjectException, BadRequestException {
        log.info("User confirm request {}", reqId);
        return requestService.confirmRequestByInitiator(userId, eventId, reqId);
    }

    @PatchMapping("/{eventId}/requests/{reqId}/reject")
    public ParticipationRequestDto rejectRequest(@PathVariable Long userId,
                                                 @PathVariable Long eventId,
                                                 @PathVariable Long reqId)
            throws ForbiddenException, IncorrectObjectException, BadRequestException {
        log.info("User reject request {}", reqId);
        return requestService.rejectRequestByInitiator(userId, eventId, reqId);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getRequests(@PathVariable Long userId,
                                                     @PathVariable Long eventId)
            throws IncorrectObjectException, BadRequestException {
        log.info("User {} get requests", userId);
        return requestService.getRequestsByInitiator(userId, eventId);
    }
}
