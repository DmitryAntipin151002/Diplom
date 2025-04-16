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
    @Column(name = "ID", columnDefinition = "uuid")
    private UUID id;

    @Column(name = "EMAIL", unique = true)
    @Email(message = "Invalid email address")
    @Size(max = 100)
    private String email;

    @Column(name = "PHONE_NUMBER")
    @Size(max = 12)
    private String phoneNumber;

    @Column(name = "password", columnDefinition = "bpchar (Types#CHAR)")
    @Size(max = 60, min = 60)
    private String encryptedPassword;

    @ManyToOne
    @JoinColumn(name = "STATUS", referencedColumnName = "ID")
    private Status status;

    @Column(name = "IS_FIRST_ENTER")
    private Boolean isFirstEnter;

    @Column(name = "END_DATE", columnDefinition = "timestamp (Types#TIMESTAMP)")
    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "role", referencedColumnName = "ID")
    private Role role;


    @Column(name = "last_login")
    private LocalDateTime lastLogin;
    
}
