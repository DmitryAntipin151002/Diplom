package AuthenticationService.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Модель данных для возврата id пользователя и токена для восстановления пароля")
public record JwtRecoveryTokenDto(

    @Schema(description = "Id пользователя")
    String userId,

    @Schema(description = "Токен восстановления")
    String recoveryToken) implements JwtToken {
}
