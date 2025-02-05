package UserService.service.Impl;



import UserService.domain.dto.UserActivityDTO;
import UserService.domain.mapper.UserActivityMapper;
import UserService.domain.model.UserActivity;

import UserService.exception.ResourceNotFoundException;

import UserService.repository.UserActivityRepository;
import UserService.service.UserActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserActivityServiceImpl implements UserActivityService {

    @Autowired
    private UserActivityRepository userActivityRepository;

    public UserActivityDTO createUserActivity(UserActivityDTO userActivityDTO) {
        UserActivity userActivity = UserActivityMapper.INSTANCE.toEntity(userActivityDTO);
        userActivity = userActivityRepository.save(userActivity);
        return UserActivityMapper.INSTANCE.toDto(userActivity);
    }

    public UserActivityDTO getUserActivityById(Integer id) {
        UserActivity userActivity = userActivityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UserActivity not found with id " + id));
        return UserActivityMapper.INSTANCE.toDto(userActivity);
    }

    public List<UserActivityDTO> getAllUserActivities() {
        List<UserActivity> userActivities = userActivityRepository.findAll();
        return userActivities.stream()
                .map(UserActivityMapper.INSTANCE::toDto)
                .toList();
    }

    public UserActivityDTO updateUserActivity(Integer id, UserActivityDTO userActivityDTO) {
        UserActivity existingActivity = userActivityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UserActivity not found with id " + id));
        existingActivity.setEventsCreated(userActivityDTO.getEventsCreated());
        existingActivity.setEventsParticipated(userActivityDTO.getEventsParticipated());
        existingActivity.setLastActive(userActivityDTO.getLastActive());
        userActivityRepository.save(existingActivity);
        return UserActivityMapper.INSTANCE.toDto(existingActivity);
    }
}
