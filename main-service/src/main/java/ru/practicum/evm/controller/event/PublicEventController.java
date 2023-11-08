package ru.practicum.evm.controller.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.evm.dto.event.EventFullDto;
import ru.practicum.evm.dto.event.EventShortDto;
import ru.practicum.evm.service.EventService;
import ru.practicum.evm.exception.IncorrectObjectException;
import ru.practicum.evm.managers.EventSort;
import ru.practicum.evm.managers.StatManager;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class PublicEventController {

    private final EventService eventService;
    private final StatManager statManager;

    @GetMapping
    public List<EventShortDto> getAllEvents(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false) String rangeStart,
            @RequestParam(required = false) String rangeEnd,
            @RequestParam(defaultValue = "false") Boolean onlyAvailable,
            @RequestParam(required = false) EventSort sort,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest httpServletRequest) {


        eventService.addViewsForEvents(eventService.getEvents(
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size));
        log.info("Public get all events");
        return eventService.getEvents(
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEventById(@PathVariable Long eventId,
                                     HttpServletRequest httpServletRequest) throws IncorrectObjectException {

        eventService.addViewForEvent(eventService.getEventById(eventId));
        log.info("Public get event {}", eventId);
        return eventService.getEventById(eventId);
    }
}
