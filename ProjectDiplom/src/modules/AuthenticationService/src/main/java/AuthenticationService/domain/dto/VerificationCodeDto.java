package AuthenticationService.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Модель данных, представляющая код верификации")
public record VerificationCodeDto(

        @Schema(description = "Код верификации")
        String code
) { }
