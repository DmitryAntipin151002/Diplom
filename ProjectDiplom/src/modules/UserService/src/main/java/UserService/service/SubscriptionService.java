package UserService.service;

import UserService.domain.dto.SubscriptionDTO;

import java.util.List;

public interface SubscriptionService {

    public SubscriptionDTO createSubscription(SubscriptionDTO subscriptionDTO);
    public SubscriptionDTO getSubscriptionById(Long id);
    public List<SubscriptionDTO> getAllSubscriptions();
    public SubscriptionDTO updateSubscription(Long id, SubscriptionDTO subscriptionDTO);
}
