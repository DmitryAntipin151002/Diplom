package UserService.domain.mapper;



import UserService.domain.dto.SubscriptionDTO;
import UserService.domain.model.Subscription;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SubscriptionMapper {
    SubscriptionMapper INSTANCE = Mappers.getMapper(SubscriptionMapper.class);

    SubscriptionDTO toDto(Subscription subscription);
    Subscription toEntity(SubscriptionDTO subscriptionDTO);
}

