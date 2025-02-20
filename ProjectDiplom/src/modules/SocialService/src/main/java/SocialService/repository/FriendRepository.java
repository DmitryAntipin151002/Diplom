package SocialService.repository;

import SocialService.model.Friend;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendRepository extends JpaRepository<Friend, Long> {
    List<Friend> findByUserId(Long userId);
    boolean existsByUserIdAndFriendId(Long userId, Long friendId);
    // Метод для удаления записи по friendId и userId (если у вас двусторонняя связь)
    void deleteByFriendIdAndUserId(Long userId, Long friendId);
}