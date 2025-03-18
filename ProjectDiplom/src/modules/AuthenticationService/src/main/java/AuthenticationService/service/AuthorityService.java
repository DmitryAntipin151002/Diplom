package AuthenticationService.service;

import AuthenticationService.domain.dto.AuthorityDto;
import AuthenticationService.domain.dto.UserDto;

import java.util.List;

public interface AuthorityService {
    /**
     * Получение списка всех доступных прав (authorities).
     *
     * @return Список объектов Authority, представляющих все доступные права в системе.
     */
    List<AuthorityDto> getAllAuthorities();

    /**
     * Добавление нового пользователя с указанными правами и ролью.
     *
     * @param userDto Объект DTO, содержащий информацию о пользователе, включая идентификаторы прав (authorities).
     *                   Должен содержать необходимые данные для создания нового пользователя.
     */
    void addUser(UserDto userDto);
}
