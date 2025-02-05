package UserService.controller;


import UserService.domain.dto.SubscriptionDTO;
import UserService.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;

    @PostMapping
    public SubscriptionDTO createSubscription(@RequestBody SubscriptionDTO subscriptionDTO) {
        return subscriptionService.createSubscription(subscriptionDTO);
    }

    @GetMapping("/{id}")
    public SubscriptionDTO getSubscriptionById(@PathVariable Long id) {
        return subscriptionService.getSubscriptionById(id);
    }

    @GetMapping
    public List<SubscriptionDTO> getAllSubscriptions() {
        return subscriptionService.getAllSubscriptions();
    }

    @PutMapping("/{id}")
    public SubscriptionDTO updateSubscription(@PathVariable Long id, @RequestBody SubscriptionDTO subscriptionDTO) {
        return subscriptionService.updateSubscription(id, subscriptionDTO);
    }
}
