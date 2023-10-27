package ru.practicum.explore.stats.controller;

import dto.HitDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.explore.stats.client.StatisticClient;

import javax.validation.Valid;
import java.time.LocalDateTime;

@Controller
@Slf4j
@Validated
@AllArgsConstructor
public class StatisticController {

    private final StatisticClient statisticClient;
    private static final String FORMAT_DATE = "yyyy-MM-dd HH:mm:ss";

    @GetMapping("/stats")
    public ResponseEntity<Object> getAllStatistic(@RequestParam
                                                  @DateTimeFormat(pattern = FORMAT_DATE)
                                                  @Valid LocalDateTime start,
                                                  @RequestParam
                                                  @DateTimeFormat(pattern = FORMAT_DATE)
                                                  @Valid LocalDateTime end,
                                                  @RequestParam String[] uris,
                                                  @RequestParam Boolean unique) {

        log.info("Get statics with parameters start={}, end={}, uris={}, unique={}", start, end, uris, unique);
        return statisticClient.getAllStatistic(start.toString(), end.toString(), uris, unique);
    }

    @PostMapping("/hit")
    public ResponseEntity<Object> create(@RequestBody @Valid HitDto hitDto) {
        log.info("Create new hit");
        return statisticClient.create(hitDto);
    }
}
