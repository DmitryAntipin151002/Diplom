package AuthenticationService.domain.mapper;

import AuthenticationService.domain.dto.SuccessfulUserLoginDto;
import AuthenticationService.domain.dto.UserDto;
import AuthenticationService.domain.dto.VerificationCodeDto;
import AuthenticationService.domain.model.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(target = "id", source = "user.id")
    @Mapping(target = "phoneNumber", source = "user.phoneNumber")
    @Mapping(target = "preAuthorizationToken", source = "preAuthorizationToken")
    @Mapping(target = "isFirstEnter", source = "user.isFirstEnter")
    SuccessfulUserLoginDto userToSuccessfulUserLoginDto(User user, String preAuthorizationToken);

    @Named("codeToVerificationCodeDto")
    default VerificationCodeDto codeToVerificationCodeDto(String code){
        return new VerificationCodeDto(code);
    }


    @Mapping(target = "endDate", source = "userDto.endDate", dateFormat = "dd.MM.yyyy")
    User map(UserDto userDto);
}
