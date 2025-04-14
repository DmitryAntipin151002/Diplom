package AuthenticationService.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Модель данных, возвращаемых при успешной аутентификации пользователя")
public record SuccessfulUserLoginDto(

        @Schema(description = "ID пользователя")
        UUID id,


        @Schema(description = "Телефонный номер пользователя")
        String phoneNumber,

        @Schema(description = "Токен предварительной авторизации")
        String preAuthorizationToken,

        @Schema(description = "Флаг первого входа")
        Boolean isFirstEnter


) { }
