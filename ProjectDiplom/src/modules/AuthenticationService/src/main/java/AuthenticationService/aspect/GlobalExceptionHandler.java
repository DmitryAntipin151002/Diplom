package AuthenticationService.aspect;

import AuthenticationService.domain.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.stream.Collectors;

import static AuthenticationService.constants.ConfigurationConstants.*;


@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException exception,
                                                               HttpServletRequest request) {
        String localizedMessage = exception.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .sorted()
                .collect(Collectors.joining(", ", "", "."));

        return errorResponseBuilder(exception, AN_ERROR_OCCURRED_WHILE_VALIDATING_DATA + localizedMessage, request);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleUserAlreadyExistsException(UserAlreadyExistsException exception,
                                                          HttpServletRequest request) {
        return errorResponseBuilder(exception, INPUT_DATA_ERROR, request);
    }

    @ExceptionHandler({UserNotFoundException.class, LoginFailException.class, VerificationCodeNotFoundException.class,
            StatusNotFoundException.class, AuthorityNotFoundException.class, RoleNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleBadRequestExceptions(RuntimeException exception, HttpServletRequest request) {
        return errorResponseBuilder(exception, TRANSMITTED_DATA_ERROR,
                THE_TRANSMITTED_DATA_IS_INCORRECT + exception.getMessage(), request);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserIsDeletedException(NotFoundException exception, HttpServletRequest request) {
        return errorResponseBuilder(exception, NOT_FOUND, request);
    }

    @ExceptionHandler(NotImplementedException.class)
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    public ErrorResponse handleNotImplementedException(NotImplementedException exception, HttpServletRequest request) {
        return errorResponseBuilder(exception, NOT_IMPLEMENTED, request);
    }

    @ExceptionHandler(TokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleUnauthorizedExceptions(TokenException exception, HttpServletRequest request) {
        return errorResponseBuilder(exception, TOKEN_ERROR, request);
    }

    @ExceptionHandler(TokenTypeException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleIncorrectTokenTypeExceptions(TokenTypeException exception, HttpServletRequest request) {
        return errorResponseBuilder(exception, TOKEN_TYPE_ERROR, request);
    }

    @ExceptionHandler(AttemptCountFailException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleAttemptCountFailExceptions(AttemptCountFailException exception,
                                                          HttpServletRequest request) {
        return errorResponseBuilder(exception, ATTEMPT_LIMIT_EXCEEDED, request);
    }



    @ExceptionHandler(UserIsBlockedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleUserIsBlockedExceptions(UserIsBlockedException exception, HttpServletRequest request) {
        return errorResponseBuilder(exception, THE_USER_IS_BLOCKED, request);
    }

    @ExceptionHandler(UserDeletedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleUserIsDeletedExceptions(UserDeletedException exception, HttpServletRequest request) {
        return errorResponseBuilder(exception, USER_DELETED, request);
    }

    @ExceptionHandler(NewPasswordMatchesOldPasswordException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse newPasswordMatchesOldPasswordException(NewPasswordMatchesOldPasswordException exception,
                                                                HttpServletRequest request) {
        return errorResponseBuilder(exception, PASSWORD_MATCH, request);
    }

    @ExceptionHandler(PasswordNotMatchesException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlePasswordNotMatchesException(PasswordNotMatchesException exception,
                                                           HttpServletRequest request) {
        return errorResponseBuilder(exception, PASSWORD_ERROR, request);
    }

    @ExceptionHandler(BadRefreshTokenException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRefreshTokenException(BadRefreshTokenException exception,
                                                        HttpServletRequest request) {
        return errorResponseBuilder(exception, PASSWORD_MATCH, request);
    }

    @ExceptionHandler(TokenExpiredException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleTokenException(TokenExpiredException exception, HttpServletRequest request) {
        return errorResponseBuilder(exception, THE_USER_IS_NOT_AUTHORIZED, request);
    }

    @ExceptionHandler(TokenMismatchException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleTokenMismatchException(TokenMismatchException exception, HttpServletRequest request) {
        return errorResponseBuilder(exception, THE_USER_IS_NOT_AUTHORIZED, request);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleHttpMessageNotReadableException(HttpMessageNotReadableException exception, HttpServletRequest request) {
        return errorResponseBuilder(exception, VALIDATION_ERROR, request);
    }

    @ExceptionHandler(SQLException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleSQLException(SQLException exception, HttpServletRequest request) {
        return errorResponseBuilder(exception, INPUT_DATA_ERROR, request);
    }

    @ExceptionHandler(DateTimeParseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleDateTimeParseException(DateTimeParseException exception, HttpServletRequest request) {
        return errorResponseBuilder(exception, VALIDATION_ERROR, request);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleConstraintViolationException(ConstraintViolationException exception, HttpServletRequest request) {
        String message = exception.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));
        return errorResponseBuilder(exception, VALIDATION_ERROR, message, request);
    }

    @Value
    @Builder
    public static class ErrorResponse {
        String title;
        String detail;
        String request;
        String time;
    }

    private String getTime() {
        String datePattern = "dd-MM-yyyy HH:mm:ss";
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(datePattern));
    }

    private ErrorResponse errorResponseBuilder(Exception exception, String title, HttpServletRequest request) {
        return errorResponseBuilder(exception, title, exception.getMessage(), request);
    }

    private ErrorResponse errorResponseBuilder(Exception exception, String title, String message, HttpServletRequest request) {
        log.info(getTime() + "\n" + exception.getMessage());
        log.info(request.getMethod() + " " + request.getRequestURI());

        return ErrorResponse.builder()
                .title(title)
                .detail(message)
                .request(request.getMethod() + " " + request.getRequestURI())
                .time(getTime())
                .build();
    }
}
