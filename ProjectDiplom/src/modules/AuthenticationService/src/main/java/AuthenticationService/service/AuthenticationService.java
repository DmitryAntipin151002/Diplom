package AuthenticationService.service;

import AuthenticationService.domain.dto.*;
import AuthenticationService.domain.model.User;

import java.util.Map;

public interface AuthenticationService {

    /**
     * Проверка зарегистрирован ли пользователь с таким логином, а так же совпадает ли зашифрованный пароль
     *
     * @param loginInfo информация для аутентификации пользователя в системе, содержащая почту и зашифрованный пароль
     * @return SuccessfulUserLoginDto - dto, содержащее информацию успешно аутентифицируемого пользователя
     */
    SuccessfulUserLoginDto authenticate(UserLoginRequestDto loginInfo);

    /**
     * Генерация 6-значного кода верификации, сохранение его в базе данных Redis вместе с идентификатором пользователя
     * и возвращение созданного кода в объекте VerificationCodeDto для отправки в смс пользователю (двухфакторная аутентификация).
     * Время жизни кода 120 секунд
     *
     * @param userId uuid пользователя
     * @return VerificationCodeDto - dto, содержащее код верификации
     */
    VerificationCodeDto generateVerificationCode(String userId);

    /**
     * Проверка сгенерированного кода верификации на валидность для завершения второго этапа аутентификации
     *
     * @param verificationCodeDto код верификации
     * @param user                модель пользователя, который прошел первый этап аутентификации
     */
    void checkVerificationCode(VerificationCodeDto verificationCodeDto, User user);

    /**
     * Замена старого пароля на новый. Проверка на то что переданные пароли валидные уже прошла в момент создания dto.
     * Теперь старый пароль сверяется с зашифрованным из базы данных. Если они совпадают то пароль меняется на новый.
     *
     * @param token             токен, переданный в заголовке
     * @param changePasswordDto dto, содержащее старый и новый пароли
     * @return зависит от того, токен какого типа передавался в заголовке:
     * - если токен первого входа в систему (isFirstEnterToken), то возвращаем AccessAndRefreshTokensDto;
     * - если токен доступа (accessToken), то просто 200 OK.
     */
    Map<String, String> changePassword(String token, ChangePasswordDto changePasswordDto);


    public SuccessfulUserRegistrationDto registerUser(UserRegistrationRequestDto registrationInfo);
}
