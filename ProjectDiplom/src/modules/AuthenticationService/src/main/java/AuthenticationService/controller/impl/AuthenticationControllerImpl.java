package AuthenticationService.controller.impl;

import AuthenticationService.controller.AuthenticationController;
import AuthenticationService.domain.dto.*;
import AuthenticationService.service.AuthenticationService;
import AuthenticationService.service.TokenService;
import AuthenticationService.service.UserRecoveryService;
import AuthenticationService.service.VerificationCodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static AuthenticationService.constants.ControllerConstants.CODE_BAD_REQUEST;
import static AuthenticationService.constants.ControllerConstants.CODE_OK;

@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RequestMapping("api/v1/authentication-service/login")
@RestController
public class AuthenticationControllerImpl implements AuthenticationController {

    private final AuthenticationService authenticationService;
    private final TokenService tokenService;
    private final UserRecoveryService userRecoveryService;
    private final VerificationCodeService verificationCodeService;

    @Override
    @PostMapping
    public ResponseEntity<SuccessfulUserLoginDto> authenticate(@RequestBody UserLoginRequestDto loginInfo) {
        return ResponseEntity.ok(authenticationService.authenticate(loginInfo));
    }

    @Override
    @PostMapping("/{userId}/code")
    public ResponseEntity<JwtToken> getTokens(@PathVariable String userId,
                                              @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                              @RequestBody VerificationCodeDto verificationCodeDto) {
        return ResponseEntity.ok(tokenService.getTokens(userId, token, verificationCodeDto));
    }

    @Override
    @PatchMapping("/password")
    public ResponseEntity<?> changePassword(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                            @RequestBody ChangePasswordDto changePasswordDto) {
        // В сервис передается ID из ChangePasswordDto
        return ResponseEntity.ok(authenticationService.changePassword(token, changePasswordDto));
    }

    @Override
    @PostMapping("/recovery")
    public ResponseEntity<JwtRecoveryTokenDto> userExistenceCheck(@RequestBody EmailAndPhoneDto emailAndPhoneDto) {
        return ResponseEntity.ok(userRecoveryService.userExistenceCheck(emailAndPhoneDto));
    }

    @Override
    @PostMapping("/recovery/password")
    public ResponseEntity<Void> updatePassword(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                               @RequestBody UpdatePasswordDto updatePasswordDto) {
        userRecoveryService.updatePassword(token, updatePasswordDto);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<SuccessfulUserRegistrationDto> register(UserRegistrationRequestDto registrationInfo) {
        SuccessfulUserRegistrationDto registrationResponse = authenticationService.registerUser(registrationInfo);
        return ResponseEntity.ok(registrationResponse);
    }

    @Override
    @Operation(summary = "Получение кода верификации")
    @ApiResponse(responseCode = CODE_OK, description = "Код верификации сгенерирован",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = VerificationCodeDto.class))})
    @ApiResponse(responseCode = CODE_BAD_REQUEST, description = "Неверные параметры запроса")
    @GetMapping("/{userId}/code")
    public ResponseEntity<VerificationCodeDto> getVerificationCode(@PathVariable String userId) {
        VerificationCodeDto verificationCodeDto = verificationCodeService.generateVerificationCode(userId);
        return new ResponseEntity<>(verificationCodeDto, HttpStatus.OK);
    }

    @Override
    @Operation(summary = "Проверка кода верификации")
    @ApiResponse(responseCode = CODE_OK, description = "Код верификации верный")
    @ApiResponse(responseCode = CODE_BAD_REQUEST, description = "Код верификации неверный")
    @PostMapping("/{userId}/code/validate")
    public ResponseEntity<String> validateVerificationCode(@PathVariable String userId,
                                                           @RequestParam String code) {
        boolean isValid = verificationCodeService.validateVerificationCode(userId, code);
        if (isValid) {
            return new ResponseEntity<>("Код верификации верный", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Неверный код верификации", HttpStatus.BAD_REQUEST);
        }
    }
}

