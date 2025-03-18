package AuthenticationService.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RedisKeyPrefix {

    ATTEMPTS("attempts:"),
    CODE("code:"),
    ROLE("role:"),
    AUTHORITIES("authorities:");

    private final String value;
}
