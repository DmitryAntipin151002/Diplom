package UserService.repository;


import UserService.model.ChatParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, UUID> {
    boolean existsByChatIdAndUserId(UUID chatId, UUID userId);
    List<ChatParticipant> findByUser_Id(UUID userId);
}