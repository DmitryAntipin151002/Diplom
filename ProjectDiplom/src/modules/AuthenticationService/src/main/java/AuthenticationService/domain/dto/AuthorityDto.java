package AuthenticationService.domain.dto;


import AuthenticationService.domain.enums.AuthorityName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Модель данных для получения списка authorities")
public record AuthorityDto(
        @Schema(description = "Id authority")
        Long id,

        @NotNull(message = "authority не может быть пустым")
        AuthorityName authority) {
}
