package ru.practicum.main.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.main.event.location.dto.LocationDto;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventRequestDto {
    @NotBlank
    @Size(min = 20, max = 2000, message = "annotation is less then 20 and more then 2000 symbols")
    private String annotation;
    @NotNull
    private Long category;
    @NotBlank
    @Size(min = 20, max = 7000, message = "description is less then 20 and more then 7000 symbols")
    private String description;
    @Future
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    @NotNull
    private LocalDateTime eventDate;
    @Valid
    @NotNull
    private LocationDto location;
    private boolean paid;
    @PositiveOrZero
    private int participantLimit = 0;
    @NotNull
    private boolean requestModeration = true;
    @NotBlank
    @Size(min = 3, max = 120, message = "title is less then 3 and more then 120 symbols")
    private String title;
}
