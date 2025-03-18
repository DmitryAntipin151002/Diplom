package AuthenticationService.domain.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Обновление JWT токена")
public record JwtRefreshTokenDto(
        @Schema(description = "userId")
        @NotNull(message = "Id пользователя не доложен быть пустым")

        String userId,

        @Schema(description = "JWT токен для обновления токена доступа (accessToken)")
        String refreshToken
) {
}
