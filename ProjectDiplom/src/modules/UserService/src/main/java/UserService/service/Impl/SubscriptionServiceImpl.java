package UserService.service.Impl;


import UserService.domain.dto.SubscriptionDTO;
import UserService.domain.mapper.SubscriptionMapper;
import UserService.domain.model.Subscription;

import UserService.exception.ResourceNotFoundException;

import UserService.repository.SubscriptionRepository;
import UserService.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Override
    public SubscriptionDTO createSubscription(SubscriptionDTO subscriptionDTO) {
        Subscription subscription = SubscriptionMapper.INSTANCE.toEntity(subscriptionDTO);
        subscription = subscriptionRepository.save(subscription);
        return SubscriptionMapper.INSTANCE.toDto(subscription);
    }

    @Override
    public SubscriptionDTO getSubscriptionById(Long id) {
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription not found with id " + id));
        return SubscriptionMapper.INSTANCE.toDto(subscription);
    }

    @Override
    public List<SubscriptionDTO> getAllSubscriptions() {
        List<Subscription> subscriptions = subscriptionRepository.findAll();
        return subscriptions.stream()
                .map(SubscriptionMapper.INSTANCE::toDto)
                .toList();
    }

    @Override
    public SubscriptionDTO updateSubscription(Long id, SubscriptionDTO subscriptionDTO) {
        Subscription existingSubscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription not found with id " + id));
        existingSubscription.setSportType(subscriptionDTO.getSportType());
        existingSubscription.setSubscribedAt(subscriptionDTO.getSubscribedAt());
        subscriptionRepository.save(existingSubscription);
        return SubscriptionMapper.INSTANCE.toDto(existingSubscription);
    }
}
