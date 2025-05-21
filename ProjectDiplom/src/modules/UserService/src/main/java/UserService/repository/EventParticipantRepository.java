package UserService.repository;

import UserService.model.Event;
import UserService.model.EventParticipant;
import UserService.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventParticipantRepository extends JpaRepository<EventParticipant, UUID> {
    List<EventParticipant> findByEventId(UUID eventId);
    Optional<EventParticipant> findByEventIdAndUserId(UUID eventId, UUID userId);
    int countByEventIdAndStatus(UUID eventId, EventParticipant.ParticipantStatus status);
    @Query("SELECT ep FROM EventParticipant ep " +
            "JOIN FETCH ep.user u " +
            "LEFT JOIN FETCH u.profile " +
            "WHERE ep.event.id = :eventId")
    List<EventParticipant> findByEventIdWithUser(@Param("eventId") UUID eventId);
    boolean existsByEventAndUser(Event event, User user);
}