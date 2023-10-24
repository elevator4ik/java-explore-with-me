package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.model.StatDto;
import ru.practicum.client.StatisticClient;
import ru.practicum.exception.ErrorException;

import javax.validation.Valid;
import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
@Slf4j
@Validated
public class StatisticController {

    private final StatisticClient statisticClient;
    private static final String FORMAT_DATE = "yyyy-MM-dd HH:mm:ss";

    @GetMapping("/stats")
    public ResponseEntity<Object> getAllStatistic(@RequestParam @DateTimeFormat(pattern = FORMAT_DATE) @Valid
                                                          LocalDateTime start,
                                                  @RequestParam @DateTimeFormat(pattern = FORMAT_DATE) @Valid
                                                          LocalDateTime end,
                                                  @RequestParam @Valid String[] uris,
                                                  @RequestParam @Valid Boolean unique) {
        if (start.isEqual(end) || start.isAfter(end)) {
            throw new ErrorException("Start is equal or after end");
        }
        log.info("Get statics with next parameters start={}, end={}, uris={}, unique={}", start, end, uris, unique);
        return statisticClient.getAllStatistic(start.toString(), end.toString(), uris, unique);
    }

    @PostMapping("/hit")
    public ResponseEntity<Object> create(@RequestBody @Valid StatDto statDto) {
        log.info("Create hit");
        return statisticClient.create(statDto);
    }
}