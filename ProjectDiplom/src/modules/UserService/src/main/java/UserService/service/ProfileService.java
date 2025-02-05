package UserService.service;


import UserService.domain.dto.request.ProfileUpdateRequest;
import UserService.domain.dto.response.UserProfileResponse;
import UserService.domain.model.Users;
import UserService.exception.ResourceNotFoundException;

public interface ProfileService {
    UserProfileResponse getProfile(Users user) throws ResourceNotFoundException;
    UserProfileResponse updateProfile(Users user, ProfileUpdateRequest request) throws ResourceNotFoundException;
    public UserProfileResponse createProfile(Users user, ProfileUpdateRequest request);
}
