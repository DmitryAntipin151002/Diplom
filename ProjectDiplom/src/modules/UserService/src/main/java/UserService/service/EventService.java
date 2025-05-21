// EventService.java
package UserService.service;

import UserService.dto.*;
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
    EventParticipantDto addParticipant(UUID eventId, UUID userId, UUID organizerId);
    void removeParticipant(UUID eventId, UUID participantId, UUID requesterId);
    List<EventParticipantDto> getParticipants(UUID eventId);
     List<ProfileDto> searchPotentialParticipants(UUID eventId, String query);

     ProfileDto getParticipantInfo(UUID participantId);
    ChatDto createEventChat(UUID eventId, String chatName, UUID creatorId);
    List<EventDto> getAllEvents();
}