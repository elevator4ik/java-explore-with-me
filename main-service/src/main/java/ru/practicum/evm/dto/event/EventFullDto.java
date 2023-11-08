package ru.practicum.evm.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.evm.dto.user.UserShortDto;
import ru.practicum.evm.model.Category;
import ru.practicum.evm.model.Location;
import ru.practicum.evm.managers.State;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventFullDto {

    private String annotation;

    private Category category;

    private Integer confirmedRequests;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;

    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn;

    private Long id;

    private UserShortDto initiator;

    private Location location;

    private boolean paid;

    private int participantLimit;

    private boolean requestModeration;

    private State state;

    private String title;

    private Long views;
}
