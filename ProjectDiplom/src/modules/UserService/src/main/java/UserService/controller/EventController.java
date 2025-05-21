package UserService.controller;

import UserService.dto.*;
import UserService.exception.AccessDeniedException;
import UserService.exception.EventNotFoundException;
import UserService.model.ChatType;
import UserService.model.Event;
import UserService.model.EventStatus;
import UserService.repository.EventRepository;
import UserService.service.ChatService;
import UserService.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;
    private final EventRepository eventRepository;
    private final ChatService chatService;

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

    @PostMapping("/{eventId}/participants")
    public ResponseEntity<EventParticipantDto> addParticipant(
            @PathVariable UUID eventId,
            @RequestParam UUID userId,
            @RequestHeader("X-User-Id") UUID organizerId) {

        // Дополнительная проверка прав
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));

        if (!event.getOrganizer().getId().equals(organizerId)) {
            throw new AccessDeniedException("Only organizer can add participants");
        }

        return ResponseEntity.ok(eventService.addParticipant(eventId, userId, organizerId));
    }

    @DeleteMapping("/{eventId}/participants/{participantId}")
    public ResponseEntity<Void> removeParticipant(
            @PathVariable UUID eventId,
            @PathVariable UUID participantId,
            @RequestHeader("X-User-Id") UUID requesterId) {
        eventService.removeParticipant(eventId, participantId, requesterId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{eventId}/participants")
    public ResponseEntity<List<EventParticipantDto>> getParticipants(
            @PathVariable UUID eventId) {
        return ResponseEntity.ok(eventService.getParticipants(eventId));
    }

    @GetMapping("/{eventId}/search-participants")
    public ResponseEntity<List<ProfileDto>> searchPotentialParticipants(
            @PathVariable UUID eventId,
            @RequestParam String query,
            @RequestHeader("X-User-Id") UUID organizerId) {

        // Проверка прав организатора
        EventDto event = eventService.getEvent(eventId);
        if (!event.getOrganizerId().equals(organizerId)) {
            throw new AccessDeniedException("Only event organizer can search participants");
        }

        return ResponseEntity.ok(eventService.searchPotentialParticipants(eventId, query));
    }


    @GetMapping("/participants/{participantId}/info")
    public ResponseEntity<ProfileDto> getParticipantInfo(
            @PathVariable UUID participantId) {
        return ResponseEntity.ok(eventService.getParticipantInfo(participantId));
    }

    @PostMapping("/event/{eventId}")
    public ResponseEntity<ChatDto> createEventChat(
            @PathVariable UUID eventId,
            @RequestParam String chatName,
            @RequestHeader("X-User-Id") UUID creatorId) {

        ChatDto chatDto = chatService.createEventChat(eventId, chatName, creatorId);
        return ResponseEntity.ok(chatDto);
    }

    @GetMapping
    public ResponseEntity<List<EventDto>> getAllEvents() {
        return ResponseEntity.ok(eventService.getAllEvents());
    }

}