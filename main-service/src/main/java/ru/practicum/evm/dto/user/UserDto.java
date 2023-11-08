package ru.practicum.evm.dto.user;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class UserDto {
    @NotBlank
    private String email;
    private long id;
    @NotBlank
    private String name;
}
