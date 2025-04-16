package UserService.repository;

import UserService.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChatRepository extends JpaRepository<Chat, UUID> {
    List<Chat> findByParticipants_UserId(UUID userId);
    Optional<Chat> findByEventId(UUID eventId);
}
