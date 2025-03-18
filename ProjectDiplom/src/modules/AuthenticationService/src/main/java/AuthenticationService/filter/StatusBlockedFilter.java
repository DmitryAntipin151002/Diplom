package AuthenticationService.filter;

import AuthenticationService.domain.exception.UserIsBlockedException;
import AuthenticationService.domain.model.User;
import lombok.extern.slf4j.Slf4j;

import static AuthenticationService.constants.FilterConstants.THE_USER_IS_BLOCKED_EXCEPTION;
import static AuthenticationService.constants.FilterConstants.THE_USER_WITH_ID_IS_BLOCKED;
import static AuthenticationService.domain.enums.StatusName.BLOCKED;

@Slf4j
public class StatusBlockedFilter extends UserStatusFilter {

    @Override
    public boolean filter(User user) {
        if (user.getStatus().getName().equals(BLOCKED)) {
            log.error(THE_USER_WITH_ID_IS_BLOCKED, user.getId());
            throw new UserIsBlockedException(THE_USER_IS_BLOCKED_EXCEPTION);
        }
        return checkNext(user);
    }
}
