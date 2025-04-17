package UserService.repository;

import UserService.model.Chat;
import UserService.model.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChatRepository extends JpaRepository<Chat, UUID> {
    List<Chat> findByParticipants_User_Id(UUID userId);
    Optional<Chat> findByIdAndParticipants_User_Id(UUID chatId, UUID userId);
    boolean existsByIdAndParticipants_User_Id(UUID chatId, UUID userId);

    @Query("SELECT CASE WHEN COUNT(cp) > 0 THEN true ELSE false END " +
            "FROM ChatParticipant cp " +
            "WHERE cp.chat.id = :chatId AND cp.user.id = :userId AND cp.isAdmin = true")
    boolean isUserAdmin(@Param("chatId") UUID chatId, @Param("userId") UUID userId);
}


