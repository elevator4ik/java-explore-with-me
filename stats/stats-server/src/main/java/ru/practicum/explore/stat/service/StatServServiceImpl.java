package ru.practicum.explore.stat.service;

import dto.HitDto;
import dto.RequestStatDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.stat.module.StatServMapper;
import ru.practicum.explore.stat.repo.StatServRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Primary
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatServServiceImpl implements StatServService {
    private final StatServRepository statRepository;
    private final StatServMapper statMapper;

    @Override
    @Transactional
    public HitDto create(HitDto hitDto) {
        return statMapper.toHitDto(statRepository.save(statMapper.toStat(hitDto)));
    }

    @Override
    public List<RequestStatDto> getAllStatistics(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        if (unique) {
            return statRepository.getAllStatistic(start, end, uris)
                    .stream()
                    .map(statMapper::toRequestStatDto)
                    .collect(Collectors.toList());
        } else {
            return statRepository.getAllStatisticNonUnique(start, end, uris)
                    .stream()
                    .map(statMapper::toRequestStatDto)
                    .collect(Collectors.toList());
        }
    }
}
