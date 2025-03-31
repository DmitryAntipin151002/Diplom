package AuthenticationService.controller;

import AuthenticationService.aspect.GlobalExceptionHandler;
import AuthenticationService.domain.dto.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static AuthenticationService.constants.ControllerConstants.*;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

@Tag(name = "Authentication controller", description = "Authentication API")
@RequestMapping("api/v1/authentication-service/login")
@Validated
public interface AuthenticationController {


    @Operation(summary = "Аутентификация пользователя по почте и паролю")
    @ApiResponse(responseCode = CODE_OK, description = OK,
            content = {@Content(mediaType = APPLICATION_JSON,
                    schema = @Schema(implementation = SuccessfulUserLoginDto.class))})
    @ApiResponse(responseCode = CODE_BAD_REQUEST, description = BAD_REQUEST,
            content = {@Content(mediaType = APPLICATION_JSON,
                    schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))})
    @ApiResponse(responseCode = CODE_NOT_FOUND, description = USER_IS_NOT_FOUND,
            content = {@Content(mediaType = APPLICATION_JSON,
                    schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))})
    @ApiResponse(responseCode = CODE_INTERNAL_SERVER_ERROR, description = INTERNAL_SERVER_ERROR,
            content = {@Content(mediaType = APPLICATION_JSON,
                    schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))})
    @PostMapping
    ResponseEntity<SuccessfulUserLoginDto> authenticate(@RequestBody @Valid UserLoginRequestDto loginInfo);


    @Operation(summary = "Получение кода верификации")
    @ApiResponse(responseCode = CODE_OK, description = OK,
            content = {@Content(mediaType = APPLICATION_JSON,
                    schema = @Schema(implementation = VerificationCodeDto.class))})
    @ApiResponse(responseCode = CODE_BAD_REQUEST, description = BAD_REQUEST,
            content = {@Content(mediaType = APPLICATION_JSON,
                    schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))})
    @ApiResponse(responseCode = CODE_NOT_FOUND, description = USER_IS_NOT_FOUND,
            content = {@Content(mediaType = APPLICATION_JSON,
                    schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))})
    @ApiResponse(responseCode = CODE_INTERNAL_SERVER_ERROR, description = INTERNAL_SERVER_ERROR,
            content = {@Content(mediaType = APPLICATION_JSON,
                    schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))})
    @GetMapping("/{userId}/code")
    ResponseEntity<VerificationCodeDto> getVerificationCode(@PathVariable  String userId);


    @Operation(summary = "Получение JWT по коду верификации")
    @ApiResponse(responseCode = CODE_OK, description = OK,
            content = {@Content(mediaType = APPLICATION_JSON,
                    schema = @Schema(implementation = VerificationCodeDto.class))})
    @ApiResponse(responseCode = CODE_BAD_REQUEST, description = BAD_REQUEST,
            content = {@Content(mediaType = APPLICATION_JSON,
                    schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))})
    @ApiResponse(responseCode = CODE_NOT_ENOUGH_RIGHTS, description = NOT_ENOUGH_RIGHTS,
            content = {@Content(mediaType = APPLICATION_JSON,
                    schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))})
    @ApiResponse(responseCode = CODE_NOT_FOUND, description = DATA_NOT_FOUND,
            content = {@Content(mediaType = APPLICATION_JSON,
                    schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))})
    @ApiResponse(responseCode = CODE_INTERNAL_SERVER_ERROR, description = INTERNAL_SERVER_ERROR,
            content = {@Content(mediaType = APPLICATION_JSON,
                    schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))})
    @PostMapping("/{userId}/code")
    ResponseEntity<JwtToken> getTokens(@PathVariable  String userId,
                                       @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                       @RequestBody @Valid VerificationCodeDto verificationCodeDto);


    @Operation(summary = "Изменение пароля пользователя")
    @ApiResponse(responseCode = CODE_OK, description = OK)
    @ApiResponse(responseCode = CODE_BAD_REQUEST, description = BAD_REQUEST,
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))})
    @ApiResponse(responseCode = CODE_THE_USER_IS_NOT_AUTHORIZED, description = THE_USER_IS_NOT_AUTHORIZED,
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))})
    @ApiResponse(responseCode = CODE_INTERNAL_SERVER_ERROR, description = INTERNAL_SERVER_ERROR,
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))})
    @PatchMapping("/password")
    ResponseEntity<?> changePassword(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                     @RequestBody @Valid ChangePasswordDto changePasswordDto);


    @Operation(summary = "Проверка существования пользователя для восстановления доступа в систему")
    @ApiResponse(responseCode = CODE_OK, description = OK)
    @ApiResponse(responseCode = CODE_BAD_REQUEST, description = BAD_REQUEST,
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))})
    @ApiResponse(responseCode = CODE_BAD_REQUEST, description = VALIDATION_FAILED,
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))})
    @ApiResponse(responseCode = CODE_NOT_FOUND, description = USER_IS_NOT_FOUND,
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))})
    @ApiResponse(responseCode = CODE_INTERNAL_SERVER_ERROR, description = INTERNAL_SERVER_ERROR,
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))})
    @PostMapping("/recovery")
    ResponseEntity<JwtRecoveryTokenDto> userExistenceCheck(@RequestBody @Valid EmailAndPhoneDto emailAndPhoneDto);


    @Operation(summary = "Отправка нового пароля при восстановлении")
    @ApiResponse(responseCode = CODE_OK, description = OK)
    @ApiResponse(responseCode = CODE_BAD_REQUEST, description = BAD_REQUEST,
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))})
    @ApiResponse(responseCode = CODE_THE_USER_IS_NOT_AUTHORIZED, description = THE_USER_IS_NOT_AUTHORIZED,
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))})
    @ApiResponse(responseCode = CODE_NOT_FOUND, description = USER_IS_NOT_FOUND,
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))})
    @ApiResponse(responseCode = CODE_INTERNAL_SERVER_ERROR, description = INTERNAL_SERVER_ERROR,
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))})
    @PostMapping("/recovery/password")
    ResponseEntity<Void> updatePassword(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                        @RequestBody @Valid UpdatePasswordDto updatePasswordDto);


    @Operation(summary = "Регистрация нового пользователя")
    @ApiResponse(responseCode = CODE_OK, description = OK,
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = SuccessfulUserRegistrationDto.class))})
    @ApiResponse(responseCode = CODE_BAD_REQUEST, description = BAD_REQUEST,
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))})
    @ApiResponse(responseCode = CODE_INTERNAL_SERVER_ERROR, description = INTERNAL_SERVER_ERROR,
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))})
    @PostMapping("/register")
    ResponseEntity<SuccessfulUserRegistrationDto> register(@RequestBody @Valid UserRegistrationRequestDto registrationInfo);

    @PostMapping("/{userId}/code/validate")
    ResponseEntity<String> validateVerificationCode(@PathVariable String userId, @RequestParam String code);
}
