package AuthenticationService.constants;

import lombok.experimental.UtilityClass;

import java.time.Duration;

@UtilityClass
public class RedisConstants {

    public static final String CODE_KEY = "code:%s";
    public static final String ATTEMPTS_KEY = "attempts:%s";
    public static final String ROLE_KEY = "role:%s";
    public static final String AUTHORITIES_KEY = "authorities:%s";

    public static final String CODE = "Code:";
    public static final String MESSAGE_EXPIRED = "message expired";

    public static final String CODE_NOT_FOUND = "Verification code not found for user ID: %s";
    public static final String ATTEMPTS_NOT_FOUND = "Attempts not found for user ID: %s";
    public static final String AUTHORITIES_EMPTY = "Authorities must not be empty for user ID: %s";
    public static final String CODE_EXPIRED = "Verification code for user ID %s has expired or does not exist";

    public static final String NO_AUTHORITIES = "User has no authorities";
    public static final String NO_ATTEMPTS = "No attempts found in Redis";

    public static final String DEFAULT_NUMBER_OF_ATTEMPTS = "3";
    public static final Duration FIVE_MINUTES = Duration.ofMinutes(5);

}
