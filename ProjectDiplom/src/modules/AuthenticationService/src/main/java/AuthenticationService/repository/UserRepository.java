package AuthenticationService.repository;

import AuthenticationService.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);

    Optional<User> findUserByEmailAndPhoneNumber(String email, String phoneNumber);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByEmail(String email);
}
