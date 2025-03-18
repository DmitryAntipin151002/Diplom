package AuthenticationService.repository;

import AuthenticationService.domain.model.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    Set<Authority> findByIdIn(Set<Long> authorityIds);
}
