package mapper;


import model.Profile;
import dto.ProfileDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProfileMapper {
    ProfileMapper INSTANCE = Mappers.getMapper(ProfileMapper.class);

    @Mapping(source = "user.id", target = "userId")
    ProfileDTO profileToProfileDTO(Profile profile);

    @Mapping(source = "userId", target = "user.id")
    Profile profileDTOToProfile(ProfileDTO profileDTO);
}
