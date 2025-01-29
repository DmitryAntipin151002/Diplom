package mapper;

import dto.request.ProfileUpdateRequest;
import dto.response.ProfileResponse;
import model.Profile;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProfileMapper {
    ProfileResponse toProfileResponse(Profile profile);
    void updateProfileFromRequest(ProfileUpdateRequest request, @MappingTarget Profile profile);
}