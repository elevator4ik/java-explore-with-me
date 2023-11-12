package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.HitDto;
import ru.practicum.dto.RequestStatDto;
import ru.practicum.dto.Validator;
import ru.practicum.exception.BadRequestException;
import ru.practicum.service.StatService;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.util.Constants.DATE_FORMAT;


@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
public class StatServiceController {

    private final StatService statService;

    @GetMapping("/stats")
    public ResponseEntity<List<RequestStatDto>> getAllStatistic(@RequestParam
                                                                @DateTimeFormat(pattern = DATE_FORMAT) LocalDateTime start,
                                                                @RequestParam
                                                                @DateTimeFormat(pattern = DATE_FORMAT) LocalDateTime end,
                                                                @RequestParam(defaultValue = "") List<String> uris,
                                                                @RequestParam(defaultValue = "false") boolean unique) {
        log.info("Calling the GET request to /stats endpoint");
        if (start != null && end != null && end.isBefore(start)) {
            throw new BadRequestException("Start is after end");
        } else {
            return new ResponseEntity<>(statService.getAllStatistics(start, end, uris, unique), HttpStatus.OK);
        }
    }

    @PostMapping("/hit")
    public ResponseEntity<HitDto> create(@RequestBody @Validated(Validator.Create.class) HitDto hitDto) {
        log.info("Calling the POST request to /hit endpoint");
        return new ResponseEntity<>(statService.create(hitDto), HttpStatus.CREATED);
    }
}
