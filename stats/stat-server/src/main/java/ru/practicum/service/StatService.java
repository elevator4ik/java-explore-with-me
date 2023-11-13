package ru.practicum.service;

import ru.practicum.dto.HitDto;
import ru.practicum.dto.RequestStatDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatService {
    HitDto create(HitDto hitDto);

    List<RequestStatDto> getAllStatistics(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
