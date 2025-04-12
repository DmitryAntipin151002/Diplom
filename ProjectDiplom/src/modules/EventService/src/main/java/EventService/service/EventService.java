
package EventService.service;

import EventService.model.Event;
import EventService.model.EventParticipant;
import EventService.model.UserProfile;
import EventService.repository.EventParticipantRepository;
import EventService.repository.EventRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;
@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final EventParticipantRepository participantRepository;
    private final RestTemplate restTemplate;

    private final String userServiceUrl = "http://localhost:8083/user-profiles/";

    public Event createEvent(Event event, UUID organizerId) {
        String url = userServiceUrl + organizerId; // Формируем правильный URL с UUID
        UserProfile profile = restTemplate.getForObject(url, UserProfile.class);
        if (profile != null) {
            event.setOrganizerId(organizerId);
            return eventRepository.save(event);
        }
        throw new RuntimeException("Organizer not found");
    }

    public Optional<Event> getEventById(Long eventId) {
        return eventRepository.findById(eventId);
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public boolean deleteEvent(Long eventId) {
        if (eventRepository.existsById(eventId)) {
            eventRepository.deleteById(eventId);
            return true;
        }
        return false;
    }

    public Event updateEvent(Long eventId, Event updatedEvent, UUID organizerId) {
        if (eventRepository.existsById(eventId)) {
            updatedEvent.setId(eventId);
            updatedEvent.setOrganizerId(organizerId);
            return eventRepository.save(updatedEvent);
        }
        throw new RuntimeException("Event not found");
    }

    public boolean joinEvent(Long eventId, UUID userId) {
        Event event = eventRepository.findById(eventId).orElseThrow();
        int participants = participantRepository.findByEventId(eventId).size();

        if (participants < event.getMaxParticipants()) {
            EventParticipant ep = new EventParticipant();
            ep.setUserId(userId);
            ep.setEvent(event);
            ep.setJoinedAt(LocalDateTime.now());
            participantRepository.save(ep);
            return true;
        }
        return false;
    }
}
