package ru.practicum.evm.controller.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.evm.dto.compilation.CompilationDto;
import ru.practicum.evm.service.CompilationService;
import ru.practicum.evm.exception.IncorrectObjectException;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/compilations")
public class PublicCompilationController {

    private final CompilationService service;

    @GetMapping
    public List<CompilationDto> findCompilations(@RequestParam(required = false) Boolean pinned,
                                                 @RequestParam(defaultValue = "0") Integer from,
                                                 @RequestParam(defaultValue = "10") Integer size) {
        log.info("Get compilations to public");
        return service.getAllCompilations(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public CompilationDto getCompilationById(@PathVariable Long compId) throws IncorrectObjectException {
        log.info("Get compilation with id {} to public", compId);
        return service.getCompilationById(compId);
    }
}