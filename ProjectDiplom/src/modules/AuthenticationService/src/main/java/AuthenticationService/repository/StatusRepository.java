package AuthenticationService.repository;

import AuthenticationService.domain.enums.StatusName;
import AuthenticationService.domain.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface StatusRepository extends JpaRepository<Status, Long> {
    @Query("select s from Status s where s.name = :name")
    Optional<Status> findByName(StatusName name);
}
