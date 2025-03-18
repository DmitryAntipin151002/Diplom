package AuthenticationService.domain.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record EmailAndPhoneDto (

        @NotNull(message = "Email не может быть пустым")
        @Schema(description = "email пользователя")
        String email,


        @NotNull(message = "Номер телефона не может быть пустым")
        @Schema(description = "Номер телефона пользователя")
        String phoneNumber
){
}
