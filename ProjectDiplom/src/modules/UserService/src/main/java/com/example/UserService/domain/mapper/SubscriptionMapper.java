package mapper;



import model.Subscription;
import dto.SubscriptionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SubscriptionMapper {
    SubscriptionMapper INSTANCE = Mappers.getMapper(SubscriptionMapper.class);

    @Mapping(source = "user.id", target = "userId")
    SubscriptionDTO subscriptionToSubscriptionDTO(Subscription subscription);

    @Mapping(source = "userId", target = "user.id")
    Subscription subscriptionDTOToSubscription(SubscriptionDTO subscriptionDTO);
}

