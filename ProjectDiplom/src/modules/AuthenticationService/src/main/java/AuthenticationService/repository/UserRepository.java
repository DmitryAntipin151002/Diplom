package AuthenticationService.repository;

import AuthenticationService.domain.model.User;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    @Query("SELECT DISTINCT u FROM User u WHERE u.email = :email")
    Optional<User> findByEmail(@Param("email")String email);



    Optional<User> findUserByEmailAndPhoneNumber(String email, String phoneNumber);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByEmail(String email);
}
