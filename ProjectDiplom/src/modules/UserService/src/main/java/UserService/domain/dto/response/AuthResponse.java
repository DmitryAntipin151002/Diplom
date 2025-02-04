package UserService.domain.dto.response;


import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AuthResponse {
    private String email;
    private String accessToken;
}
