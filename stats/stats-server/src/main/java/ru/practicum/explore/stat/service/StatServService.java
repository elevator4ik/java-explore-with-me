package ru.practicum.explore.stat.service;

import dto.HitDto;
import dto.RequestStatDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatServService {
    HitDto create(HitDto hitDto);

    List<RequestStatDto> getAllStatistics(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
