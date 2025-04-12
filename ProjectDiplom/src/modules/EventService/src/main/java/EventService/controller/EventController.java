// controller/EventController.java
package EventService.controller;

import EventService.model.Event;
import EventService.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    // Создание мероприятия с использованием UUID в URL
    @PostMapping("/create/{organizerId}")
    public Event create(@RequestBody Event event, @PathVariable UUID organizerId) {
        return eventService.createEvent(event, organizerId);
    }

    @GetMapping
    public List<Event> all() {
        return eventService.getAllEvents();
    }

    @GetMapping("/{id}")
    public Optional<Event> get(@PathVariable Long id) {
        return eventService.getEventById(id);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return eventService.deleteEvent(id);
    }

    @PutMapping("/{id}")
    public Event update(@PathVariable Long id, @RequestBody Event event, @RequestParam UUID organizerId) {
        return eventService.updateEvent(id, event, organizerId);
    }

    @PostMapping("/{id}/join")
    public boolean join(@PathVariable Long id, @RequestParam UUID userId) {
        return eventService.joinEvent(id, userId);
    }
}
