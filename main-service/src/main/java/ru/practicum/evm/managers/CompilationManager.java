package ru.practicum.evm.managers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.evm.model.Compilation;
import ru.practicum.evm.repository.CompilationEventRepository;
import ru.practicum.evm.repository.CompilationRepository;
import ru.practicum.evm.exception.IncorrectObjectException;
import ru.practicum.evm.exception.ForbiddenException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationManager {

    private final CompilationRepository compilationRepository;
    private final CompilationEventRepository compilationEventRepository;

    public void compilationExist(Long compId) throws IncorrectObjectException {
        final List<Compilation> allCompilations = compilationRepository.findAll();
        List<Long> ids = new ArrayList<>();
        if (!allCompilations.isEmpty()) {
            ids = allCompilations.stream()
                    .map(Compilation::getId)
                    .collect(Collectors.toList());
        }
        if (allCompilations.isEmpty() || !ids.contains(compId)) {
            throw new IncorrectObjectException("No compilation with id " + compId);
        }
    }

    public void rePinned(Long compId) throws ForbiddenException {
        if (compilationRepository.getReferenceById(compId).getPinned()) {
            throw new ForbiddenException("Compilation with id " + compId + " already pinned");
        }
    }

    public void noPinned(Long compId) throws ForbiddenException {
        if (!compilationRepository.getReferenceById(compId).getPinned()) {
            throw new ForbiddenException("Compilation with id " + compId + " is no pinned");
        }
    }

    public void eventInCompilation(Long compId, Long eventId) {
        if (!compilationEventRepository.existsByCompilationIdAndEventId(compId, eventId)) {
            throw new IllegalArgumentException("Event id = " + eventId + " is not in compilation id = " + compId);
        }
    }
}
