package AuthenticationService.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ServiceConstants {
    public static final String EMAIL = "email ";
    public static final String ATTEMPTS = "Количество попыток: {}";
    public static final String ID = "id ";
    public static final String PASSWORD_FOR_EMAIL_EXCEPTION = "Пароль для email ";
    public static final String THE_CODE_IS_INVALID_ATTEMPTS_REMAINING_EXCEPTION = "Код не верный, осталось попыток: ";
    public static final String ZERO_ATTEMPT_REMAINING_ID_IS_BLOCKED_EXCEPTION = "Код не верный, осталось 0 попыток. Id %s заблокирован";
    public static final String USER_ID_WITH_THIS_REFRESH_TOKEN_NOT_FOUND_USER_ID_EXCEPTION = "userId с таким refreshToken не найден";
    public static final String USER_WITH_EMAIL_AND_PHONE_NUMBER_NOT_FOUND_EXCEPTION = "email: %s phoneNumber: %s";
    public static final String SEARCH_FOR_A_USER_WITH_EMAIL = "Поиск пользователя с email {}";
    public static final String SEARCH_FOR_EMAIL_WAS_SUCCESSFUL = "Поиск для {} прошел успешно";
    public static final String THE_TRANSMITTED_DATA_WAS_INCORRECT_FOR_EMAIL = "Переданные данные были некорректными для {}";
    public static final String CODE_DOES_NOT_MATCH_ATTEMPTS_REMAINING = "Код {} не совпадает, осталось попыток: {}";
    public static final String ZERO_ATTEMPT_REMAINING_ID_IS_BLOCKED = "Осталось {} попыток, id {} заблокирован";
    public static final String THE_PASSWORD_DOES_NOT_MATCH_THE_PASSWORD_FROM_THE_DB = "Пароль не совпадает с паролем из БД";
    public static final String PASSWORD_CHANGED_SUCCESSFULLY = "Пароль успешно изменен";
    public static final String THE_NEW_PASSWORD_MATCHES_THE_OLD_PASSWORD_FROM_THE_DB = "Новый пароль совпадает со старым паролем из БД";
    public static final String JWT_TOKEN_UPDATED_SUCCESSFULLY = "JWT токен успешно обновлен";
    public static final String USER_WITH_EMAIL_WAS_NOT_FOUND = "Пользователь с email {} не был найден";
    public static final String USER_WITH_ID_WAS_NOT_FOUND = "Пользователь с id {} не был найден";
    public static final String USER_WITH_EMAIL_AND_PHONE_NUMBER_NOT_FOUND = "Пользователь с email {} и phoneNumber {} не был найден";
}
