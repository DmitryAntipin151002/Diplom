package AuthenticationService.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ControllerConstants {
    public static final String CODE_OK = "200";
    public static final String OK = "OK";
    public static final String CODE_BAD_REQUEST = "400";
    public static final String BAD_REQUEST = "BAD REQUEST";
    public static final String CODE_THE_USER_IS_NOT_AUTHORIZED = "401";
    public static final String THE_USER_IS_NOT_AUTHORIZED = "Пользователь не авторизован";
    public static final String CODE_NOT_ENOUGH_RIGHTS = "403";
    public static final String NOT_ENOUGH_RIGHTS = "Недостаточно прав";
    public static final String CODE_NOT_FOUND = "404";
    public static final String USER_IS_NOT_FOUND = "Пользователь не найден";
    public static final String DATA_NOT_FOUND = "Данные не найдены";
    public static final String CODE_INTERNAL_SERVER_ERROR ="500";
    public static final String INTERNAL_SERVER_ERROR = "Внутренняя ошибка сервера";
    public static final String VALIDATION_FAILED = "Валидация не пройдена";
}
