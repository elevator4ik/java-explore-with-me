package ru.practicum.evm.dto.compilation;

import lombok.Builder;
import ru.practicum.evm.dto.event.EventShortDto;

import javax.validation.constraints.NotNull;
import java.util.List;

@Builder
public class CompilationDto {
    private List<EventShortDto> events;
    @NotNull
    private long id;
    @NotNull
    private boolean pinned;
    @NotNull
    private String title;
}
