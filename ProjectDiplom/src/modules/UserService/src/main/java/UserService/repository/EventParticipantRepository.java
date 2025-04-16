package UserService.repository;

import UserService.model.EventParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventParticipantRepository extends JpaRepository<EventParticipant, UUID> {
    List<EventParticipant> findByEventId(UUID eventId);
    Optional<EventParticipant> findByEventIdAndUserId(UUID eventId, UUID userId);
    int countByEventIdAndStatus(UUID eventId, EventParticipant.ParticipantStatus status);
}