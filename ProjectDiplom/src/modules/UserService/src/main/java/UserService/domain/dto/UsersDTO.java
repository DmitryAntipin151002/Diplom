package UserService.domain.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UsersDTO {
    public Long userId;
    public String email;
    public String passwordHash;
    public LocalDateTime createdAt;
}