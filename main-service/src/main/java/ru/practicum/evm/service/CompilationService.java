package ru.practicum.evm.service;

import ru.practicum.evm.dto.compilation.CompilationDto;
import ru.practicum.evm.dto.compilation.NewCompilationDto;
import ru.practicum.evm.exception.IncorrectObjectException;
import ru.practicum.evm.exception.ForbiddenException;

import java.util.List;

public interface CompilationService {

    CompilationDto createCompilation(NewCompilationDto newCompilationDto) throws IncorrectObjectException;

    CompilationDto getCompilationById(Long compId) throws IncorrectObjectException;

    List<CompilationDto> getAllCompilations(Boolean pinned, int from, int size);

    void deleteCompilationById(Long compId) throws IncorrectObjectException;

    void addEventToCompilation(Long compId, Long eventId) throws IncorrectObjectException;

    void deleteEventFromCompilation(Long compId, Long eventId) throws IncorrectObjectException;

    void pinCompilationById(Long compId) throws IncorrectObjectException, ForbiddenException;

    void unpinCompilationById(Long compId) throws IncorrectObjectException, ForbiddenException;
}
