package ru.practicum.service;

import ru.practicum.model.RequestStat;
import ru.practicum.model.StatDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatisticsService {
    void saveHit(StatDto statDto);

    List<RequestStat> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
