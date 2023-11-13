package ru.practicum.main.event.location.dto;


import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LocationDto {
    private double lat;
    private double lon;
}
