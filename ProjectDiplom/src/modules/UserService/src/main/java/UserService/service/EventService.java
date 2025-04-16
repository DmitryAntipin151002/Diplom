// EventService.java
package UserService.service;

import UserService.dto.EventCreateDto;
import UserService.dto.EventDto;
import UserService.model.EventStatus;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface EventService {
    @Transactional
    EventDto createEvent(UUID organizerId, EventCreateDto createDto);

    EventDto getEvent(UUID eventId);

    List<EventDto> getEventsByOrganizer(UUID organizerId);
    List<EventDto> getUpcomingEvents();
    EventDto updateEventStatus(UUID eventId, EventStatus status);
    void deleteEvent(UUID eventId);
}