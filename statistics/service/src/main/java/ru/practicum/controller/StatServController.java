package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.RequestStat;
import ru.practicum.model.StatDto;
import ru.practicum.service.StatisticsService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StatServController {

    private final StatisticsService statsService;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @GetMapping("/stats")
    public List<RequestStat> getAllStatistic(@RequestParam String start,
                                             @RequestParam String end,
                                             @RequestParam(required = false) List<String> uris,
                                             @RequestParam(required = false, defaultValue = "false") Boolean unique) {
        log.info("Start to get statistic from repo with parameters unique = {} start = {} end = {}", unique, start, end);

        return statsService.getStats(LocalDateTime.parse(start, DATE_TIME_FORMATTER),
                LocalDateTime.parse(end, DATE_TIME_FORMATTER), uris, unique);
    }

    @PostMapping("/hit")
    public void create(@RequestBody StatDto statDto) {
        log.info("Start to save hit");
        statsService.saveHit(statDto);
    }
}
