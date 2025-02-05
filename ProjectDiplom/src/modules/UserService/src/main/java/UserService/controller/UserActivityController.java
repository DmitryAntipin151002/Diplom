package UserService.controller;


import UserService.domain.dto.UserActivityDTO;
import UserService.service.UserActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-activity")
public class UserActivityController {

    @Autowired
    private UserActivityService userActivityService;

    @PostMapping
    public UserActivityDTO createUserActivity(@RequestBody UserActivityDTO userActivityDTO) {
        return userActivityService.createUserActivity(userActivityDTO);
    }

    @GetMapping("/{id}")
    public UserActivityDTO getUserActivityById(@PathVariable Integer id) {
        return userActivityService.getUserActivityById(id);
    }

    @GetMapping
    public List<UserActivityDTO> getAllUserActivities() {
        return userActivityService.getAllUserActivities();
    }

    @PutMapping("/{id}")
    public UserActivityDTO updateUserActivity(@PathVariable Integer id, @RequestBody UserActivityDTO userActivityDTO) {
        return userActivityService.updateUserActivity(id, userActivityDTO);
    }
}
