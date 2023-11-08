package ru.practicum.evm.mapper;

import org.springframework.stereotype.Service;
import ru.practicum.evm.dto.category.CategoryDto;
import ru.practicum.evm.model.Category;
import ru.practicum.evm.dto.event.EventFullDto;
import ru.practicum.evm.dto.event.EventShortDto;
import ru.practicum.evm.dto.event.NewEventDto;
import ru.practicum.evm.model.Event;
import ru.practicum.evm.model.Location;
import ru.practicum.evm.dto.user.UserShortDto;
import ru.practicum.evm.managers.State;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class EventMapper {

    public Event toEvent(NewEventDto newEventDto) {
        Event event = new Event();
        event.setAnnotation(newEventDto.getAnnotation());
        event.setDescription(newEventDto.getDescription());
        event.setTitle(newEventDto.getTitle());
        event.setEventDate(newEventDto.getEventDate());
        event.setPaid(newEventDto.getPaid());
        event.setParticipantLimit(newEventDto.getParticipantLimit());
        event.setRequestModeration(newEventDto.getRequestModeration());
        event.setLat(newEventDto.getLocation().getLat());
        event.setLon(newEventDto.getLocation().getLon());
        event.setConfirmedRequests(0);
        event.setCreatedOn(LocalDateTime.now());
        event.setState(State.PENDING);
        event.setViews(0L);
        event.setPublishedOn(LocalDateTime.now());
        return event;
    }

    public EventFullDto toEventFullDto(Event event) {
        return EventFullDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(new Category(event.getCategory().getId(), event.getCategory().getName()))
                .confirmedRequests(event.getConfirmedRequests())
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .initiator(new UserShortDto(event.getInitiator().getId(), event.getInitiator().getName()))
                .location(new Location(event.getLat(), event.getLon()))
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .views(event.getViews()).build();
    }

    public List<EventFullDto> toListOfEventFullDto(Iterable<Event> events) {
        List<EventFullDto> eventFullDtos = new ArrayList<>();
        for (Event event : events) {
            eventFullDtos.add(toEventFullDto(event));
        }
        return eventFullDtos;
    }


    public EventShortDto toEventShortDto(Event event) {
        return EventShortDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(new CategoryDto(event.getCategory().getId(), event.getCategory().getName()))
                .eventDate(event.getEventDate())
                .initiator(new UserShortDto(event.getInitiator().getId(), event.getInitiator().getName()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }

    public List<EventShortDto> toListOfEventShortDto(Iterable<Event> events) {
        List<EventShortDto> eventShortDtos = new ArrayList<>();
        for (Event event : events) {
            eventShortDtos.add(toEventShortDto(event));
        }
        return eventShortDtos;
    }
}