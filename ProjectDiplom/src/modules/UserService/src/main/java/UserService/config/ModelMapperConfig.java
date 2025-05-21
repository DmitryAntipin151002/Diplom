package UserService.config;

import UserService.dto.ChatDto;
import UserService.dto.RelationshipDto;
import UserService.model.Chat;
import UserService.model.ChatType;
import UserService.model.UserRelationship;
import org.modelmapper.AbstractConverter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.modelmapper.Converters.Collection.map;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        configureGeneralSettings(modelMapper);
        addConverters(modelMapper);
        addCustomMappings(modelMapper);
        return modelMapper;
    }

    private void configureGeneralSettings(ModelMapper modelMapper) {
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setAmbiguityIgnored(true);
    }

    private void addConverters(ModelMapper modelMapper) {
        modelMapper.addConverter(new AbstractConverter<String, ChatType>() {
            @Override
            protected ChatType convert(String source) {
                return ChatType.valueOf(source.toUpperCase());
            }
        });
    }

    private void addCustomMappings(ModelMapper modelMapper) {
        modelMapper.createTypeMap(UserRelationship.class, RelationshipDto.class)
                .addMappings(mapper -> {
                    mapper.map(src -> src.getType().getName(), RelationshipDto::setType);
                    mapper.map(src -> src.getStatus().getName(), RelationshipDto::setStatus);
                    mapper.map(src -> src.getUser().getId(), RelationshipDto::setUserId);
                    mapper.map(src -> src.getRelatedUser().getId(), RelationshipDto::setRelatedUserId);
                });
    }


    }
