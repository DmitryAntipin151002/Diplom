package AuthenticationService.filter;

import AuthenticationService.domain.exception.UserIsBlockedException;
import AuthenticationService.domain.model.User;
import lombok.extern.slf4j.Slf4j;

import static AuthenticationService.constants.FilterConstants.THE_USER_IS_BLOCKED_EXCEPTION;
import static AuthenticationService.constants.FilterConstants.THE_USER_WITH_ID_IS_BLOCKED;
import static AuthenticationService.domain.enums.StatusName.ACTIVE;

@Slf4j
public class StatusActiveFilter extends UserStatusFilter {

    @Override
    public boolean filter(User user) {
        // Проверка на неактивный статус
        if (!user.getStatus().getName().equals(ACTIVE)) {
            log.error(THE_USER_WITH_ID_IS_BLOCKED, user.getId());  // Логируем, что пользователь заблокирован
            throw new UserIsBlockedException(THE_USER_IS_BLOCKED_EXCEPTION);  // Сообщение о блокировке
        }
        return checkNext(user);
    }
}
