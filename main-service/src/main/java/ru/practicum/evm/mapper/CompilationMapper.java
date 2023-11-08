package ru.practicum.evm.mapper;

import org.springframework.stereotype.Service;
import ru.practicum.evm.dto.compilation.CompilationDto;
import ru.practicum.evm.dto.compilation.NewCompilationDto;
import ru.practicum.evm.model.Compilation;
import ru.practicum.evm.dto.event.EventShortDto;

import java.util.List;

@Service
public class CompilationMapper {

    public CompilationDto toDto(Compilation compilation, List<EventShortDto> events) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .title(compilation.getTitle())
                .pinned(compilation.getPinned())
                .events(events)
                .build();
    }

    public Compilation toCompilation(NewCompilationDto newCompilationDto) {
        return Compilation.builder()
                .title(newCompilationDto.getTitle())
                .pinned(newCompilationDto.getPinned())
                .build();
    }
}
