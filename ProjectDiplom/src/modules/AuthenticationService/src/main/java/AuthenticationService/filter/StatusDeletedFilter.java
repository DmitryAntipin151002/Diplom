package AuthenticationService.filter;

import AuthenticationService.domain.exception.UserDeletedException;
import AuthenticationService.domain.model.User;
import lombok.extern.slf4j.Slf4j;

import static AuthenticationService.constants.FilterConstants.THE_USER_DELETED_EXCEPTION;
import static AuthenticationService.constants.FilterConstants.THE_USER_WITH_ID_HAS_BEEN_DELETED;
import static AuthenticationService.domain.enums.StatusName.DELETED;

@Slf4j
public class StatusDeletedFilter extends UserStatusFilter {
    @Override
    public boolean filter(User user) {
        if (user.getStatus().getName().equals(DELETED)) {
            log.error(THE_USER_WITH_ID_HAS_BEEN_DELETED, user.getId());
            throw new UserDeletedException(THE_USER_DELETED_EXCEPTION);
        }
        return checkNext(user);
    }
}
