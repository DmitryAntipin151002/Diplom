package AuthenticationService.domain.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Schema(description = "Модель данных для замены старого пароля на новый")
public record ChangePasswordDto(


        @NotNull(message = "Старый пароль не может быть пустым")
        @Schema(description = "Старый пароль")
        String password,


        @NotNull(message = "Новый пароль не может быть пустым")
        @Schema(description = "Новый пароль")
        String newPassword) {
}
