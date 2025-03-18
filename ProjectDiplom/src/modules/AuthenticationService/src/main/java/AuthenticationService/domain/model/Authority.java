package AuthenticationService.domain.model;

import AuthenticationService.domain.enums.AuthorityName;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "AUTHORITY")
public class Authority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "NAME")
    @Enumerated(EnumType.STRING)
    AuthorityName authority;
}
