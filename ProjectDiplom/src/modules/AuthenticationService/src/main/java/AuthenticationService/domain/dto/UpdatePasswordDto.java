package AuthenticationService.domain.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Модель данных для отправки нового пароля при восстановлении в системе")
public record UpdatePasswordDto (

        @NotNull(message = "Новый пароль не может быть пустым")
        @Schema(description = "Новый пароль")
        String newPassword
){
}
