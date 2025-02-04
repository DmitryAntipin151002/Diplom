package UserService.service.Impl;

import UserService.domain.dto.request.ProfileUpdateRequest;
import UserService.domain.dto.response.UserProfileResponse;
import UserService.domain.mapper.UserProfileMapper;
import UserService.domain.model.UserProfile;
import UserService.domain.model.Users;
import UserService.exception.ResourceNotFoundException;
import UserService.repository.ProfileRepository;
import UserService.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;
    private final UserProfileMapper profileMapper;

    @Override
    public UserProfileResponse getProfile(Users user) throws ResourceNotFoundException {
        UserProfile profile = profileRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));
        return profileMapper.toUserProfileResponse(profile);
    }

    @Override
    public UserProfileResponse updateProfile(Users user, ProfileUpdateRequest request) throws ResourceNotFoundException {
        UserProfile profile = profileRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));

        profileMapper.updateUserProfileFromRequest(request, profile);
        UserProfile updatedProfile = profileRepository.save(profile);
        return profileMapper.toUserProfileResponse(updatedProfile);
    }
}
