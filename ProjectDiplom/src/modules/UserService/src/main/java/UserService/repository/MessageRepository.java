package UserService.repository;



import UserService.model.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {
    List<Message> findByChatIdOrderBySentAtDesc(UUID chatId, Pageable pageable);

    @Modifying
    @Query("UPDATE Message m SET m.isRead = true, m.readAt = :readAt " +
            "WHERE m.chat.id = :chatId AND m.sender.id != :userId AND m.isRead = false")
    void markMessagesAsRead(UUID chatId, UUID userId, LocalDateTime readAt);

    Optional<Message> findTopByChatIdOrderBySentAtDesc(UUID chatId);
}