package AuthenticationService.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "USERS")
@Data
@Accessors(chain = true)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "EMAIL", unique = true, nullable = false, length = 100)
    @Email(message = "Invalid email address")
    @Size(max = 100)
    private String email;

    @Column(name = "PHONE_NUMBER", length = 12)
    @Size(max = 12)
    private String phoneNumber;

    @Column(name = "PASSWORD", length = 60, nullable = false)
    @Size(min = 60, max = 60)
    private String encryptedPassword;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STATUS", referencedColumnName = "ID")
    private Status status;

    @Column(name = "IS_FIRST_ENTER")
    private Boolean isFirstEnter;

    @Column(name = "END_DATE")
    private LocalDate endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ROLE", referencedColumnName = "ID")
    private Role role;

    @Column(name = "LAST_LOGIN")
    private LocalDateTime lastLogin;
}

