package AuthenticationService.domain.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;
import java.util.UUID;

@Schema(description = "Модель данных отправки в Authentication service")
public record UserDto(
        @Schema(description = "Id пользователя")
        UUID id,


        @Schema(description = "Электронная почта")
        @NotBlank(message = "Email не может быть пустым")
        @NotNull(message = "Email не может быть пустым")
        String email,


        @Schema(description = "Номер телефона")
        @NotBlank(message = "PhoneNumber не может быть пустым")
        @NotNull(message = "PhoneNumber не может быть пустым")
        String phoneNumber,

    
        @Schema(description = "Дата окончания срока прав")
        @NotBlank(message = "EndDate не может быть пустым")
        @NotNull(message = "EndDate не может быть пустым")
        String endDate,

        @Schema(description = "Зашифрованный пароль")
        @Size(max = 60, min = 60)
        String encryptedPassword,

        @Schema(description = "ID authorities")
        Set<Long> authorityIds) {
}
