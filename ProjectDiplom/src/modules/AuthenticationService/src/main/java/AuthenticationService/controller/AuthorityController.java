package AuthenticationService.controller;

import AuthenticationService.aspect.GlobalExceptionHandler;
import AuthenticationService.domain.dto.AuthorityDto;
import AuthenticationService.domain.dto.SuccessfulUserRegistrationDto;
import AuthenticationService.domain.dto.UserDto;
import AuthenticationService.domain.dto.UserRegistrationRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import static AuthenticationService.constants.ControllerConstants.*;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

@Tag(name = "Authority controller", description = "Role model API")
@RequestMapping("api/v1/authentication-service")
@Validated
public interface AuthorityController {
    /**
     * Получение формы для присвоения Authorities пользователю
     * <a href="https://wiki.astondevs.ru/pages/viewpage.action?pageId=114447178">
     * AuthS-8</a>
     *
     * @return Список объектов Authority, содержащих информацию об authorities
     * @author Artem Depresov
     */
    @Operation(summary = "Получение формы для присвоения Authorities пользователю")
    @ApiResponse(responseCode = CODE_OK, description = OK,
            content = {@Content(mediaType = APPLICATION_JSON,
                    schema = @Schema(implementation = AuthorityDto.class))})
    @ApiResponse(responseCode = CODE_BAD_REQUEST, description = BAD_REQUEST,
            content = {@Content(mediaType = APPLICATION_JSON,
                    schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))})
    @ApiResponse(responseCode = CODE_THE_USER_IS_NOT_AUTHORIZED, description = THE_USER_IS_NOT_AUTHORIZED,
            content = {@Content(mediaType = APPLICATION_JSON,
                    schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))})
    @ApiResponse(responseCode = CODE_NOT_FOUND, description = DATA_NOT_FOUND,
            content = {@Content(mediaType = APPLICATION_JSON,
                    schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))})
    @ApiResponse(responseCode = CODE_INTERNAL_SERVER_ERROR, description = INTERNAL_SERVER_ERROR,
            content = {@Content(mediaType = APPLICATION_JSON,
                    schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))})
    @GetMapping("/get-authorities")
    ResponseEntity<List<AuthorityDto>> getAllAuthorities();

    /**
     * Добавление пользователя и его прав в базу данных
     *
     * @param userDto Модель данных, содержащая информацию для добавления пользователя
     * @author Artem Depresov
     */
    @Operation(summary = "Внутренний эндпоинт - добавление пользователя и его прав в базу данных")
    @ApiResponse(responseCode = CODE_OK, description = OK)
    @ApiResponse(responseCode = CODE_BAD_REQUEST, description = BAD_REQUEST,
            content = {@Content(mediaType = APPLICATION_JSON,
                    schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))})
    @ApiResponse(responseCode = CODE_THE_USER_IS_NOT_AUTHORIZED, description = THE_USER_IS_NOT_AUTHORIZED,
            content = {@Content(mediaType = APPLICATION_JSON,
                    schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))})
    @ApiResponse(responseCode = CODE_INTERNAL_SERVER_ERROR, description = INTERNAL_SERVER_ERROR,
            content = {@Content(mediaType = APPLICATION_JSON,
                    schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))})
    @PostMapping("/add-user")
    ResponseEntity<Void> addUser(@RequestBody UserDto userDto);



}
