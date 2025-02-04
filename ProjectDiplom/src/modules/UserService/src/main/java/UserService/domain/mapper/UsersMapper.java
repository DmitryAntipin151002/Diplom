package UserService.domain.mapper;

import UserService.domain.dto.UsersDTO;
import UserService.domain.model.Users;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface UsersMapper {

    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "passwordHash", target = "passwordHash")
    @Mapping(source = "createdAt", target = "createdAt")
    UsersDTO toDto(Users user);

    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "passwordHash", target = "passwordHash")
    @Mapping(source = "createdAt", target = "createdAt")
    Users toEntity(UsersDTO usersDTO);
}