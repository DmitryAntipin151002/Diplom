package UserService.repository;

import UserService.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID> {
    @Query("SELECT e FROM Event e " +
            "WHERE (:sportType IS NULL OR e.sportType = :sportType) " +
            "AND (:location IS NULL OR e.location = :location) " +
            "AND (e.organizer.id IN :friendIds OR EXISTS " +
            "   (SELECT p FROM EventParticipant p WHERE p.event = e AND p.user.id IN :friendIds)) " +
            "AND e.startTime > CURRENT_TIMESTAMP " +
            "ORDER BY e.startTime ASC")
    List<Event> findRecommendedEvents(
            @Param("sportType") String sportType,
            @Param("location") String location,
            @Param("friendIds") List<UUID> friendIds);

    List<Event> findByOrganizerId(UUID organizerId);
    List<Event> findByStartTimeAfterOrderByStartTimeAsc(LocalDateTime date);
}