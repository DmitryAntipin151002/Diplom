package AuthenticationService.domain.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SuccessfulUserRegistrationDto {

    private String userId;
    private String email;


    // Getters and Setters
}

