package AuthenticationService.service;

import AuthenticationService.domain.dto.EmailAndPhoneDto;
import AuthenticationService.domain.dto.JwtRecoveryTokenDto;
import AuthenticationService.domain.dto.UpdatePasswordDto;

public interface UserRecoveryService {

    /**
     * Проверка существования пользователя на основе переданных почтового адреса и номера телефона для создания
     * токена восстановления (recoveryToken), который необходим для создания нового пароля
     * @param emailAndPhoneDto dto, содержащее электронную почту и телефон пользователя
     * @return RecoveryTokenDto - dto, содержащее id пользователя и токен восстановления
     */
    JwtRecoveryTokenDto userExistenceCheck(EmailAndPhoneDto emailAndPhoneDto);

    /**
     * Отправка нового пароля при восстановлении в системе
     * @param updatePasswordDto dto, содержащее новый пароль пользователя
     */
    void updatePassword(String token, UpdatePasswordDto updatePasswordDto);
}
