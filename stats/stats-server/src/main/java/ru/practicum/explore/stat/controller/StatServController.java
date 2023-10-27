package ru.practicum.explore.stat.controller;

import dto.RequestStatDto;
import dto.HitDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.stat.service.StatServService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class StatServController {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final StatServService statService;

    @GetMapping("/stats")
    public List<RequestStatDto> getAllStatistic(@RequestParam String start, @RequestParam String end,
                                                @RequestParam(required = false) List<String> uris,
                                                @RequestParam(required = false, defaultValue = "false") Boolean unique) {

        return statService.getAllStatistics(LocalDateTime.parse(start, DATE_TIME_FORMATTER),
                LocalDateTime.parse(end, DATE_TIME_FORMATTER), uris, unique);
    }

    @PostMapping("/hit")
    public HitDto create(@RequestBody HitDto hitDto) {
        return statService.create(hitDto);
    }
}