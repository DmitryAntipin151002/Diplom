package AuthenticationService.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SecurityConstants {
    public static final String REFRESH_TOKEN_HAS_EXPIRED_EXCEPTION = "RefreshToken срок действия истек";
    public static final String REFRESH_TOKEN_NULL_OR_EMPTY = "RefreshToken null или пустой";
    public static final String REFRESH_TOKEN_PARSE_ERROR = "Ошибка при попытке расшифровки refreshToken";
    public static final String PRIVATE_KEY_EXCEPTION = "Приватный ключ ";
    public static final String PUBLIC_KEY_EXCEPTION = "Публичный ключ ";
    public static final String TYPE = "type";
    public static final String IS_FIRST_ENTER_TOKEN = "IS_FIRST_ENTER_TOKEN";
    public static final String PRE_AUTHORIZE = "PRE_AUTHORIZE";
    public static final String RECOVERY = "RECOVERY";
    public static final String ID = "id";
    public static final String ACCESS = "ACCESS";
    public static final String REFRESH = "REFRESH";
    public static final String BEARER = "Bearer ";
    public static final String INVALID_PRIVATE_KEY = "Неверный приватный ключ: ";
    public static final String INVALID_PUBLIC_KEY = "Неверный публичный ключ: ";
    public static final String RSA = "RSA";
}
