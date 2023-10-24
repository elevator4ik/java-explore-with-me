package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.model.RequestStat;
import ru.practicum.model.StatDto;
import ru.practicum.model.StatMapper;
import ru.practicum.repository.StatRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatisticsServiceImpl implements StatisticsService {
    private final StatMapper statMapper;
    private final StatRepository statRepository;

    @Override
    @Transactional
    public void saveHit(StatDto statDto) {
        statRepository.saveAndFlush(statMapper.toStat(statDto));

    }

    @Override
    public List<RequestStat> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        if (unique) {
            return statRepository.getAllStatistic(start, end, uris);
        } else {
            return statRepository.getAllStatisticNonUnique(start, end, uris);
        }
    }
}