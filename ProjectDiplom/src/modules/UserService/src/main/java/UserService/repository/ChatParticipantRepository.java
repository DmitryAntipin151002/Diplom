package UserService.repository;


import UserService.model.ChatParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, UUID> {
    @Query("SELECT COUNT(cp) > 0 FROM ChatParticipant cp WHERE cp.chat.id = :chatId AND cp.user.id = :userId")
    boolean existsByChatIdAndUserId(@Param("chatId") UUID chatId, @Param("userId") UUID userId);
    List<ChatParticipant> findByUser_Id(UUID userId);
}