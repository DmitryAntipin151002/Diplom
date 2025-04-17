package UserService.repository;



import UserService.model.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {
    List<Message> findByChat_IdOrderBySentAtAsc(UUID chatId, Pageable pageable);

    @Query("SELECT m FROM Message m WHERE m.chat.id = :chatId AND m.sender.id = :userId AND m.status = 'SENT'")
    List<Message> findUnreadMessages(@Param("chatId") UUID chatId, @Param("userId") UUID userId);

    @Modifying
    @Query("UPDATE Message m SET m.status = 'READ', m.readAt = :now WHERE m.id IN :messageIds")
    void markMessagesAsRead(@Param("messageIds") List<UUID> messageIds, @Param("now") LocalDateTime now);

    Page<Message> findByChatIdOrderBySentAtDesc(UUID chatId, Pageable pageable);


}