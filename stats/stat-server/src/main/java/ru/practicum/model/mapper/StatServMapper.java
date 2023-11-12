package ru.practicum.model.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.dto.HitDto;
import ru.practicum.dto.RequestStatDto;
import ru.practicum.model.RequestStat;
import ru.practicum.model.Stat;

@Component
public class StatServMapper {

    public Stat toStat(HitDto hitDto) {
        return Stat.builder()
                .ip(hitDto.getIp())
                .uri(hitDto.getUri())
                .timestamp(hitDto.getTimestamp())
                .app(hitDto.getApp())
                .build();
    }

    public HitDto toHitDto(Stat stat) {
        return HitDto.builder()
                .timestamp(stat.getTimestamp())
                .app(stat.getApp())
                .uri(stat.getUri())
                .ip(stat.getIp())
                .build();
    }

    public RequestStatDto toRequestStatDto(RequestStat request) {
        return RequestStatDto.builder()
                .app(request.getApp())
                .uri(request.getUri())
                .hits(request.getHits())
                .build();
    }
}
