package UserService.service;

import UserService.domain.dto.UserActivityDTO;

import java.util.List;

public interface UserActivityService {

    public UserActivityDTO createUserActivity(UserActivityDTO userActivityDTO);
    public UserActivityDTO getUserActivityById(Integer id);
    public List<UserActivityDTO> getAllUserActivities();
    public UserActivityDTO updateUserActivity(Integer id, UserActivityDTO userActivityDTO);
}
