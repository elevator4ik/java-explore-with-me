package ru.practicum.main.user.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserShortDto {
    @NotNull
    private Long id;
    @NotBlank
    private String name;
}
