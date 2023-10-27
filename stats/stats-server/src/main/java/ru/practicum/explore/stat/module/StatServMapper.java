package ru.practicum.explore.stat.module;

import dto.HitDto;
import dto.RequestStatDto;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class StatServMapper {
    public Stat toStat(HitDto dto) {
        Stat stat = new Stat();
        stat.setApp(dto.getApp());
        stat.setIp(dto.getIp());
        stat.setUri(dto.getUri());
        if (dto.getTimeStamp() == null) {
            stat.setDateTimeIncome(LocalDateTime.now());
        } else {
            stat.setDateTimeIncome(dto.getTimeStamp());
        }
        return stat;
    }

    public HitDto toHitDto(Stat stat) {
        return new HitDto(
                stat.getDateTimeIncome(),
                stat.getUri(),
                stat.getIp(),
                stat.getApp());
    }

    public RequestStatDto toRequestStatDto(RequestStat stat) {
        return new RequestStatDto(
                stat.getApp(),
                stat.getUri(),
                stat.getHit());
    }
}