package ru.practicum.evm.controller.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.evm.dto.event.EventFullDto;
import ru.practicum.evm.dto.event.UpdateEventAdminRequestDto;
import ru.practicum.evm.service.EventService;
import ru.practicum.evm.exception.BadRequestException;
import ru.practicum.evm.exception.IncorrectObjectException;
import ru.practicum.evm.exception.ForbiddenException;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("admin/events")
public class AdminEventController {
    private final EventService eventService;

    @PutMapping("/{eventId}")
    public EventFullDto updateEvent(@PathVariable Long eventId,
                                    @RequestBody UpdateEventAdminRequestDto eventRequest) throws IncorrectObjectException, BadRequestException {
        log.info("Admin update event {}", eventId);
        return eventService.updateEventByAdmin(eventId, eventRequest);
    }

    @PatchMapping("/{eventId}/publish")
    public EventFullDto publishEvent(@PathVariable Long eventId) throws ForbiddenException, IncorrectObjectException {
        log.info("Admin publish event {}", eventId);
        return eventService.publishEventByAdmin(eventId);
    }

    @PatchMapping("/{eventId}/reject")
    public EventFullDto rejectEvent(@PathVariable Long eventId) throws ForbiddenException, IncorrectObjectException {
        log.info("Admin reject event {}", eventId);
        return eventService.rejectEventByAdmin(eventId);
    }

    @GetMapping
    public List<EventFullDto> getEvents(@RequestParam(required = false) List<Long> users,
                                        @RequestParam(required = false) List<String> states,
                                        @RequestParam(required = false) List<Long> categories,
                                        @RequestParam(required = false) String rangeStart,
                                        @RequestParam(required = false) String rangeEnd,
                                        @RequestParam(defaultValue = "0") int from,
                                        @RequestParam(defaultValue = "10") int size) {
        log.info("Admin get events");
        return eventService.getEventsByAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }
}
