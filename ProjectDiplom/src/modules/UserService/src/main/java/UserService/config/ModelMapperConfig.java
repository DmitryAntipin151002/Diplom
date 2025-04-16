package UserService.config;

import UserService.dto.ProfileDto;
import UserService.model.UserProfile;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        configureModelMapper(modelMapper);
        return modelMapper;
    }

    private void configureModelMapper(ModelMapper modelMapper) {
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setAmbiguityIgnored(true);

        modelMapper.typeMap(UserProfile.class, ProfileDto.class)
                .addMappings(mapper -> {
                    mapper.map(src -> src.getUser().getId(), ProfileDto::setUserId);
                    // Уберите эту строку, если она вызывает проблемы
                    // mapper.skip(ProfileDto::setUserId);
                });
    }
}