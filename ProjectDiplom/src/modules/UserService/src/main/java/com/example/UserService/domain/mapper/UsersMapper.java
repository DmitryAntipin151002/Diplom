package mapper;
import dto.UsersDTO;
import model.Users;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface UsersMapper {
    UsersDTO toDto(Users user);
}


