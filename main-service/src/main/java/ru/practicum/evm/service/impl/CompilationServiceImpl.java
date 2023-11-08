package ru.practicum.evm.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.evm.dto.compilation.CompilationDto;
import ru.practicum.evm.dto.compilation.NewCompilationDto;
import ru.practicum.evm.mapper.CompilationMapper;
import ru.practicum.evm.model.Compilation;
import ru.practicum.evm.model.CompilationEvent;
import ru.practicum.evm.repository.CompilationEventRepository;
import ru.practicum.evm.repository.CompilationRepository;
import ru.practicum.evm.exception.IncorrectObjectException;
import ru.practicum.evm.exception.ForbiddenException;
import ru.practicum.evm.managers.CompilationManager;
import ru.practicum.evm.managers.EventManager;
import ru.practicum.evm.service.CompilationService;
import ru.practicum.evm.service.EventService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final CompilationManager compilationManager;
    private final CompilationRepository compilationRepository;
    private final CompilationMapper compilationMapper;
    private final CompilationEventRepository compilationEventRepository;
    private final EventManager eventManager;
    private final EventService eventService;

    @Override
    public CompilationDto createCompilation(NewCompilationDto newCompilationDto) throws IncorrectObjectException {
        for (Long eventId : newCompilationDto.getEvents()) {
            eventManager.eventExist(eventId);
        }
        final Compilation newCompilation = compilationRepository.save(compilationMapper.toCompilation(newCompilationDto));
        final Long compId = newCompilation.getId();
        for (Long eventId : newCompilationDto.getEvents()) {
            compilationEventRepository.save(new CompilationEvent(null, compId, eventId));
        }
        return compilationMapper.toDto(newCompilation, eventService.getEventsByCompilationId(compId));
    }

    @Override
    public CompilationDto getCompilationById(Long compId) throws IncorrectObjectException {
        compilationManager.compilationExist(compId);
        return compilationMapper.toDto(
                compilationRepository.getReferenceById(compId), eventService.getEventsByCompilationId(compId));
    }

    @Override
    public List<CompilationDto> getAllCompilations(Boolean pinned, int from, int size) {
        return compilationRepository.findAllByParams(pinned, from, size).stream()
                .map(compilation -> compilationMapper.toDto(compilation, eventService.getEventsByCompilationId(compilation.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteCompilationById(Long compId) throws IncorrectObjectException {
        compilationManager.compilationExist(compId);
        compilationRepository.deleteById(compId);
    }

    @Override
    public void addEventToCompilation(Long compId, Long eventId) throws IncorrectObjectException {
        compilationManager.compilationExist(compId);
        eventManager.eventExist(eventId);
        compilationEventRepository.save(new CompilationEvent(null, compId, eventId));
    }

    @Override
    public void deleteEventFromCompilation(Long compId, Long eventId) throws IncorrectObjectException {
        compilationManager.compilationExist(compId);
        eventManager.eventExist(eventId);
        compilationManager.eventInCompilation(compId, eventId);
        compilationEventRepository.deleteByCompilationIdAndEventId(compId, eventId);
    }

    @Override
    public void pinCompilationById(Long compId) throws IncorrectObjectException, ForbiddenException {
        compilationManager.compilationExist(compId);
        compilationManager.rePinned(compId);
        Compilation pinnedCompilation = compilationRepository.getReferenceById(compId);
        pinnedCompilation.setPinned(true);
        compilationRepository.save(pinnedCompilation);
    }

    @Override
    public void unpinCompilationById(Long compId) throws IncorrectObjectException, ForbiddenException {
        compilationManager.compilationExist(compId);
        compilationManager.noPinned(compId);
        Compilation pinnedCompilation = compilationRepository.getReferenceById(compId);
        pinnedCompilation.setPinned(false);
        compilationRepository.save(pinnedCompilation);
    }
}
