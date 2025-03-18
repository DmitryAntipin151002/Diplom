package AuthenticationService.service;

import AuthenticationService.domain.model.User;

public interface UserService {
    User findByEmail(String email);

    User findById(String id);

    User findUserByEmailAndPhoneNumber(String email, String phoneNumber);

    boolean existsByPhone(String phone);

    boolean existsByEmail(String email);

    User save(User user);

}
