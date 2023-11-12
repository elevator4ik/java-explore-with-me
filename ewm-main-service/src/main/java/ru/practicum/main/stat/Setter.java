package ru.practicum.main.stat;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.main.event.model.Event;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor
public class Setter {
    private final StatService statService;

    public void setViewAndRequests(List<Event> events) {
        Map<Long, Long> confirmedRequest = statService.toConfirmedRequest(events);
        Map<Long, Long> view = statService.toView(events);

        for (Event event : events) {
            event.setConfirmedRequests(confirmedRequest.getOrDefault(event.getId(), 0L));
            event.setView(view.getOrDefault(event.getId(), 0L));
        }
    }
    public int toConfirmedRequest(Event event) {
        return statService.toConfirmedRequest(List.of(event)).values().size();
    }

    public void setViewAndRequestsAndAddHit(List<Event> events, HttpServletRequest request) {
        setViewAndRequests(events);
        statService.addHits(request);
    }
}
