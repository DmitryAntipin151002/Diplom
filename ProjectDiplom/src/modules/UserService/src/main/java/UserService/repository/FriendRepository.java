package UserService.repository;

import UserService.domain.model.Friend;
import UserService.domain.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {
    List<Friend> findByUser(Users user);
    List<Friend> findByFriend(Users friend);
    Optional<Friend> findByUserAndFriend(Users user, Users friend);
    @Query("SELECT f FROM Friend f WHERE f.user.id = :userId AND f.friend.id = :friendId")
    Optional<Friend> findByUserAndFriendId(@Param("userId") Long userId, @Param("friendId") Long friendId);

}
