package UserService.repository;

import UserService.domain.model.UserActivity;
import UserService.domain.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserActivityRepository extends JpaRepository<UserActivity, Integer> {
    UserActivity findByUser(Users user);
}
