package ru.practicum.evm.controller.requesst;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.evm.exception.BadRequestException;
import ru.practicum.evm.exception.IncorrectObjectException;
import ru.practicum.evm.exception.ForbiddenException;
import ru.practicum.evm.dto.request.ParticipationRequestDto;
import ru.practicum.evm.service.RequestService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class RequestController {

    private final RequestService service;

    @PostMapping("/{userId}/requests")
    public ParticipationRequestDto addRequest(@PathVariable("userId") Long userId,
                                              @RequestParam ("eventId") Long eventId)
            throws ForbiddenException, IncorrectObjectException, BadRequestException {
        log.info("Add Request");
        return service.createRequest(userId, eventId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(@PathVariable(value = "userId") Long userId,
                                                 @PathVariable(value = "requestId") Long requestId)
            throws IncorrectObjectException, ForbiddenException, BadRequestException {
        log.info("Cancel request");
        return service.cancelRequest(userId, requestId);
    }

    @GetMapping("/{userId}/requests")
    public List<ParticipationRequestDto> getRequestsByRequesterId(@PathVariable(value = "userId") Long userId)
            throws IncorrectObjectException {
        log.info("Get request by requester id");
        return service.getRequestsByRequesterId(userId);
    }
}
