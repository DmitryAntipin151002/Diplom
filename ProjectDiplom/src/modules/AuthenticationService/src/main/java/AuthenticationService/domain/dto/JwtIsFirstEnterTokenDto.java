package AuthenticationService.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Модель данных для возврата токена аутентификации при первом входе в систему")
public record JwtIsFirstEnterTokenDto(

    @Schema(description = "Токен аутентификации при первом входе в систему")
    String isFirstEnterToken) implements JwtToken {
}
