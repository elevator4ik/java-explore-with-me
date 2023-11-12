package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.HitDto;
import ru.practicum.dto.RequestStatDto;
import ru.practicum.model.mapper.StatServMapper;
import ru.practicum.repository.StatServiceRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Primary
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatServiceImpl implements StatService {
    private final StatServiceRepository statRepository;
    private final StatServMapper statMapper;

    @Override
    @Transactional
    public HitDto create(HitDto hitDto) {
        return statMapper.toHitDto(statRepository.save(statMapper.toStat(hitDto)));
    }

    @Override
    public List<RequestStatDto> getAllStatistics(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        if (uris.isEmpty()){
            uris = null;
        }
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