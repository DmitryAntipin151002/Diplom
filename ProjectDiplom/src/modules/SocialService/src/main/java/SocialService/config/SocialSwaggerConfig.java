package SocialService.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Social Service API",
                version = "v1",
                description = "Social Service API Documentation",
                license = @License(name = "MIT License")
        )
)
@Configuration
public class SocialSwaggerConfig {

    @Bean
    public GroupedOpenApi socialApi() {
        return GroupedOpenApi.builder()
                .group("social")
                .packagesToScan("SocialService.controller")  // Укажите пакеты, где находятся ваши контроллеры
                .build();
    }
}
