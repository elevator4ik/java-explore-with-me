package ru.practicum.model;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class StatMapper {
    public Stat toStat(StatDto dto) {
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
}
