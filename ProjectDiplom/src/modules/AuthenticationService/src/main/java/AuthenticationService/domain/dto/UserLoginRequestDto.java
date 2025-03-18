package AuthenticationService.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Модель данных, переданных для входа в систему")
public record UserLoginRequestDto(

        @Schema(description = "Электронная почта пользователя")
        @NotBlank(message = "Поле почты не может быть пустым")

        String email,

        @Schema(description = "Пароль пользователя")
        @NotBlank(message = "Поле пароля не может быть пустым")

        String password
) { }
