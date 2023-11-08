package ru.practicum.evm.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.evm.dto.event.*;
import ru.practicum.evm.model.Category;
import ru.practicum.evm.repository.CategoryRepository;
import ru.practicum.evm.repository.CompilationEventRepository;
import ru.practicum.evm.mapper.EventMapper;
import ru.practicum.evm.model.Event;
import ru.practicum.evm.repository.EventRepository;
import ru.practicum.evm.exception.BadRequestException;
import ru.practicum.evm.exception.IncorrectObjectException;
import ru.practicum.evm.exception.ForbiddenException;
import ru.practicum.evm.model.User;
import ru.practicum.evm.repository.UserRepository;
import ru.practicum.evm.managers.*;
import ru.practicum.evm.service.EventService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventManager eventManager;
    private final EventRepository eventRepository;
    private final UserManager userManager;
    private final CategoryManager categoryManager;
    private final EventMapper eventMapper;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final CompilationEventRepository compilationEventRepository;

    @Override
    public EventFullDto createEvent(NewEventDto newEventDto, Long userId)
            throws BadRequestException, IncorrectObjectException {
        final Long categoryId = newEventDto.getCategory();
        eventManager.isEventDateBeforeTwoHours(newEventDto.getEventDate());
        userManager.checkUserExists(userId);
        categoryManager.categoryExist(categoryId);
        final Event event = eventMapper.toEvent(newEventDto);
        final User initiator = userRepository.findById(userId).get();
        final Category category = categoryRepository.getReferenceById(categoryId);
        event.setInitiator(initiator);
        event.setCategory(category);
        event.setState(State.PENDING);
        return eventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    public EventFullDto updateEvent(UpdateEventUserRequestDto updateEventUserRequestDto, Long userId)
            throws IncorrectObjectException, BadRequestException, ForbiddenException {
        final Long eventId = updateEventUserRequestDto.getEventId();
        final Long categoryId = updateEventUserRequestDto.getCategory();
        userManager.checkUserExists(userId);
        eventManager.eventExist(eventId);
        eventManager.eventInitiator(eventId, userId);
        categoryManager.categoryExist(categoryId);
        eventManager.eventPublishedState(eventId);
        eventManager.isEventDateBeforeTwoHours(updateEventUserRequestDto.getEventDate());
        final Event event = eventRepository.getReferenceById(eventId);
        event.setAnnotation(updateEventUserRequestDto.getAnnotation());
        event.setCategory(categoryRepository.getReferenceById(updateEventUserRequestDto.getCategory()));
        event.setDescription(updateEventUserRequestDto.getDescription());
        event.setEventDate(updateEventUserRequestDto.getEventDate());
        event.setPaid(updateEventUserRequestDto.getPaid());
        event.setParticipantLimit(updateEventUserRequestDto.getParticipantLimit());
        event.setTitle(updateEventUserRequestDto.getTitle());
        if (event.getState().equals(State.CANCELLING)) {
            event.setState(State.PENDING);
        }
        return eventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    public EventFullDto cancelEvent(Long userId, Long eventId) throws IncorrectObjectException, BadRequestException {
        userManager.checkUserExists(userId);
        eventManager.eventExist(eventId);
        eventManager.eventInitiator(eventId, userId);
        Event event = eventRepository.getReferenceById(eventId);
        event.setState(State.CANCELLING);
        return eventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    public EventFullDto getEventByInitiator(Long userId, Long eventId) throws IncorrectObjectException, BadRequestException {
        userManager.checkUserExists(userId);
        eventManager.eventExist(eventId);
        eventManager.eventInitiatorIsNot(eventId, userId);
        return eventMapper.toEventFullDto(eventRepository.findAllByInitiatorIdAndId(userId, eventId));
    }

    @Override
    public List<EventFullDto> getEventsByInitiator(Long userId, int from, int size) throws IncorrectObjectException {
        userManager.checkUserExists(userId);
        eventManager.checkCorrectParams(from, size);
        return eventMapper.toListOfEventFullDto(eventRepository.findAllByInitiatorId(userId, PageRequest.of(from, size)));
    }

    @Override
    public List<EventShortDto> getEvents(String text,
                                         List<Long> categories,
                                         Boolean paid,
                                         String rangeStart,
                                         String rangeEnd,
                                         Boolean onlyAvailable,
                                         ru.practicum.evm.managers.EventSort sort,
                                         int from,
                                         int size) {
        eventManager.checkCorrectParams(from, size);
        LocalDateTime startTime;
        LocalDateTime endTime;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (rangeStart == null) {
            startTime = LocalDateTime.now();
        } else {
            startTime = LocalDateTime.parse(rangeStart, formatter);
        }
        if (rangeStart == null) {
            endTime = LocalDateTime.now().plusYears(100);
        } else {
            endTime = LocalDateTime.parse(rangeEnd, formatter);
        }
        List<Event> events;
        if (categories == null) {
            if (onlyAvailable) {
                events = eventRepository.findEventsByManyParamsWithoutCatAvailable(
                        text, paid, startTime, endTime, from, size);
            } else {
                events = eventRepository.findEventsByManyParamsWithoutCat(
                        text, paid, startTime, endTime, from, size);
            }
        } else {
            if (onlyAvailable) {
                events = eventRepository.findEventsByManyParamsWithCatAvailable(
                        text, categories, paid, startTime, endTime, from, size);
            } else {
                events = eventRepository.findEventsByManyParamsWithCat(
                        text, categories, paid, startTime, endTime, from, size);
            }
        }
        if (sort != null) {
            if (sort == EventSort.EVENT_DATE) {
                events = events.stream().sorted(Comparator.comparing(Event::getEventDate)).collect(Collectors.toList());
            }
            if (sort == EventSort.VIEWS) {
                events = events.stream().sorted(Comparator.comparing(Event::getViews)).collect(Collectors.toList());
            }
        }
        return eventMapper.toListOfEventShortDto(events);
    }

    @Override
    public EventFullDto getEventById(Long eventId) throws IncorrectObjectException {
        eventManager.eventExist(eventId);
        return eventMapper.toEventFullDto(eventRepository.getByIdPublished(eventId));
    }

    @Override
    public EventFullDto updateEventByAdmin(Long eventId, UpdateEventAdminRequestDto updateEventAdminRequest) throws IncorrectObjectException, BadRequestException {
        eventManager.eventExist(eventId);
        Event event = eventRepository.getById(eventId);
        eventManager.isEventDateBeforeTwoHours(event.getEventDate());
        Category category = categoryRepository.getById(updateEventAdminRequest.getCategory());
        if (updateEventAdminRequest.getAnnotation() != null) {
            event.setAnnotation(updateEventAdminRequest.getAnnotation());
        }
        if (updateEventAdminRequest.getCategory() != null) {
            event.setCategory(category);
        }
        if (updateEventAdminRequest.getDescription() != null) {
            event.setDescription(updateEventAdminRequest.getDescription());
        }
        if (updateEventAdminRequest.getEventDate() != null) {
            event.setEventDate(updateEventAdminRequest.getEventDate());
        }
        if (updateEventAdminRequest.getLocation() != null) {
            event.setLat(updateEventAdminRequest.getLocation().getLat());
            event.setLon(updateEventAdminRequest.getLocation().getLon());
        }
        if (updateEventAdminRequest.getPaid() != null) {
            event.setPaid(updateEventAdminRequest.getPaid());
        }
        if (updateEventAdminRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventAdminRequest.getParticipantLimit());
        }
        if (updateEventAdminRequest.getRequestModeration() != null) {
            event.setRequestModeration(updateEventAdminRequest.getRequestModeration());
        }
        if (updateEventAdminRequest.getTitle() != null) {
            event.setTitle(updateEventAdminRequest.getTitle());
        }
        event.setState(State.PENDING);

        return eventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    public EventFullDto publishEventByAdmin(Long eventId)
            throws IncorrectObjectException, ForbiddenException {
        eventManager.eventExist(eventId);
        Event event = eventRepository.getReferenceById(eventId);
        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
            throw new ForbiddenException("Start time of event must be at least 1 hour from now");
        }
        if (!event.getState().equals(State.PENDING)) {
            throw new ForbiddenException("Event must be in state PENDING to be published");
        }
        event.setState(State.PUBLISHED);
        return eventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    public EventFullDto rejectEventByAdmin(Long eventId)
            throws IncorrectObjectException, ForbiddenException {
        eventManager.eventExist(eventId);
        Event event = eventRepository.getById(eventId);
        if (!event.getState().equals(State.PENDING)) {
            throw new ForbiddenException("Event must not be in state PENDING to be rejected");
        }
        event.setState(State.CANCELLING);
        return eventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    public List<EventFullDto> getEventsByAdmin(List<Long> users,
                                               List<String> states,
                                               List<Long> categories,
                                               String rangeStart,
                                               String rangeEnd,
                                               int from,
                                               int size) {
        eventManager.checkCorrectParams(from, size);
        LocalDateTime startTime;
        LocalDateTime endTime;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (rangeStart == null) {
            startTime = LocalDateTime.now();
        } else {
            startTime = LocalDateTime.parse(rangeStart, formatter);
        }
        if (rangeStart == null) {
            endTime = LocalDateTime.now().plusYears(100);
        } else {
            endTime = LocalDateTime.parse(rangeEnd, formatter);
        }

        List<State> eventStates = null;
        if (states != null) {
            eventStates = states.stream().map(State::valueOf).collect(Collectors.toList());
        }

        List<Event> events = eventRepository.findEventsByManyParamsByAdmin(
                users, eventStates, categories, startTime, endTime);
        if (events.size() < size) {
            size = events.size();
        }
        return eventMapper.toListOfEventFullDto(events.subList(from, from/size));
    }

    @Override
    public List<EventShortDto> getEventsByCompilationId(Long compilationId) {
        List<Long> eventsIds = compilationEventRepository.getCompilationEventIds(compilationId);
        return eventMapper.toListOfEventShortDto(eventRepository.getAllByIds(eventsIds));
    }

    @Override
    public void addViewsForEvents(List<EventShortDto> eventsShortDtos) {
        for (EventShortDto eventShortDto : eventsShortDtos) {
            Event event = eventRepository.getById(eventShortDto.getId());
            event.setViews(event.getViews() + 1);
            eventRepository.save(event);
        }
    }

    @Override
    public void addViewForEvent(EventFullDto eventFullDto) {
        Event event = eventRepository.getById(eventFullDto.getId());
        event.setViews(event.getViews() + 1);
        eventRepository.save(event);
    }
}
