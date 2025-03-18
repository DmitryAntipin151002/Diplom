package AuthenticationService.configuration;

import AuthenticationService.filter.StatusBlockedFilter;
import AuthenticationService.filter.StatusDeletedFilter;
import AuthenticationService.filter.UserStatusFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfiguration {

    @Bean
    public UserStatusFilter getFilter() {
        return UserStatusFilter.link(new StatusBlockedFilter(), new StatusDeletedFilter());
    }
}
