package ru.practicum.evm.service;

import ru.practicum.evm.dto.event.*;
import ru.practicum.evm.exception.BadRequestException;
import ru.practicum.evm.exception.IncorrectObjectException;
import ru.practicum.evm.exception.ForbiddenException;
import ru.practicum.evm.managers.EventSort;

import java.sql.SQLException;
import java.util.List;

public interface EventService {

    EventFullDto createEvent(NewEventDto newEventDto, Long userId) throws BadRequestException, IncorrectObjectException, SQLException;

    EventFullDto updateEvent(UpdateEventUserRequestDto updateEventUserRequestDto, Long userId) throws IncorrectObjectException, BadRequestException, ForbiddenException;

    EventFullDto cancelEvent(Long userId, Long eventId) throws IncorrectObjectException, BadRequestException;

    EventFullDto getEventByInitiator(Long userId, Long eventId) throws IncorrectObjectException, BadRequestException;

    List<EventFullDto> getEventsByInitiator(Long userId, int from, int size) throws IncorrectObjectException;

    List<EventShortDto> getEvents(String text,
                                  List<Long> categories,
                                  Boolean paid,
                                  String rangeStart,
                                  String rangeEnd,
                                  Boolean onlyAvailable,
                                  EventSort eventSort,
                                  int from,
                                  int size);

    EventFullDto getEventById(Long eventId) throws IncorrectObjectException;

    EventFullDto updateEventByAdmin(Long eventId, UpdateEventAdminRequestDto updateEventAdminRequest) throws IncorrectObjectException, BadRequestException;

    EventFullDto publishEventByAdmin(Long eventId) throws IncorrectObjectException, ForbiddenException;

    EventFullDto rejectEventByAdmin(Long eventId) throws IncorrectObjectException, ForbiddenException;

    List<EventFullDto> getEventsByAdmin(List<Long> users,
                                        List<String> states,
                                        List<Long> categories,
                                        String rangeStart,
                                        String rangeEnd,
                                        int from,
                                        int size);

    List<EventShortDto> getEventsByCompilationId(Long compilationId);

    void addViewsForEvents(List<EventShortDto> eventsShortDtos);

    void addViewForEvent(EventFullDto eventFullDto);
}
