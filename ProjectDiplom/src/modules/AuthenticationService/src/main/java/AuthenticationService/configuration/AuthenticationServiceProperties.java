package AuthenticationService.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "authentication-service")
public class AuthenticationServiceProperties {
    private Duration codeTtl = Duration.ofMinutes(5);
    private Duration roleTtl = Duration.ofMinutes(5);
    private Duration authoritiesTtl = Duration.ofMinutes(5);
    private int numberOfAttempts = 5;
}

