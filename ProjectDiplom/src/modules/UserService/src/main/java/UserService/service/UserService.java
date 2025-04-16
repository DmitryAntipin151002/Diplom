// UserService.java
package UserService.service;

import UserService.dto.UserDto;
import UserService.dto.UserUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UserService {
    UserDto getUserById(UUID userId);
    Page<UserDto> getAllUsers(Pageable pageable);
    UserDto updateUser(UUID userId, UserUpdateDto updateDto);
}