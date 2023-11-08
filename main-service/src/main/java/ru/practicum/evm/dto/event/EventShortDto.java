package ru.practicum.evm.dto.event;

import lombok.Builder;
import lombok.Data;
import ru.practicum.evm.dto.user.UserShortDto;
import ru.practicum.evm.dto.category.CategoryDto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Builder
@Data
public class EventShortDto {

    private Long id;
    @NotEmpty
    private String annotation;
    @NotNull
    private CategoryDto category;
    @NotEmpty
    private LocalDateTime eventDate;
    @NotEmpty
    private UserShortDto initiator;
    @NotEmpty
    private boolean paid;
    @NotEmpty
    private String title;
    private Long views;
}
