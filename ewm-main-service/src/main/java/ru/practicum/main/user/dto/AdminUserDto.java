package ru.practicum.main.user.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminUserDto {
    private Long id;
    private String name;
    private String email;
}
