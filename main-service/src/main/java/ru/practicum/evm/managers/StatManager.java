package ru.practicum.evm.managers;

import dto.HitDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explore.stats.client.StatisticClient;


import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class StatManager {

    private final StatisticClient statsClient = new StatisticClient("${stats-server.url}");

    public void collect(HttpServletRequest request) {
        HitDto hitDto = HitDto.builder()
                .app("ewm-service")
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timeStamp(LocalDateTime.now())
                .build();
        sendStat(hitDto);
    }

    private void sendStat(HitDto hitDto) {
        statsClient.create(hitDto);
    }
}
