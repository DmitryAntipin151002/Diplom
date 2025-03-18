package AuthenticationService.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Модель данных для отображения пары токенов доступа и обновления")
public record JwtAccessAndRefreshDto(
        @Schema(description = "JWT токен доступа")
        String accessToken,

        @Schema(description = "JWT токен обновления")
        String refreshToken
) implements JwtToken {
}
