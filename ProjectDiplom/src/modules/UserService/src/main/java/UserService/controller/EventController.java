package UserService.controller;

import UserService.dto.EventCreateDto;
import UserService.dto.EventDto;
import UserService.model.EventStatus;
import UserService.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @PostMapping
    public ResponseEntity<EventDto> createEvent(
            @RequestHeader("X-User-Id") UUID organizerId,
            @RequestBody EventCreateDto createDto) {
        EventDto eventDto = eventService.createEvent(organizerId, createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(eventDto);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventDto> getEvent(@PathVariable UUID eventId) {
        return ResponseEntity.ok(eventService.getEvent(eventId));
    }

    @GetMapping("/organizer/{organizerId}")
    public ResponseEntity<List<EventDto>> getEventsByOrganizer(@PathVariable UUID organizerId) {
        return ResponseEntity.ok(eventService.getEventsByOrganizer(organizerId));
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<EventDto>> getUpcomingEvents() {
        return ResponseEntity.ok(eventService.getUpcomingEvents());
    }

    @PatchMapping("/{eventId}/status")
    public ResponseEntity<EventDto> updateEventStatus(
            @PathVariable UUID eventId,
            @RequestParam EventStatus status) {
        return ResponseEntity.ok(eventService.updateEventStatus(eventId, status));
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable UUID eventId) {
        eventService.deleteEvent(eventId);
        return ResponseEntity.noContent().build();
    }
}