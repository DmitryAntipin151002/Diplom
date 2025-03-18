package AuthenticationService.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Модель данных для отображения данных после удачно пройденного этапа 2FA")
public record SuccessfulUserAuthenticationDto(

    @Schema(description = "JWT токен доступа")
    String accessToken,

    @Schema(description = "JWT токен обновления")
    String refreshToken,

    @Schema(description = "Статус пользователя")
    Long statusId) implements JwtToken {
}
