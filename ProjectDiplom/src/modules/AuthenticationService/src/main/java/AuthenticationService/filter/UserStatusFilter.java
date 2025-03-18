package AuthenticationService.filter;

import AuthenticationService.domain.model.User;

public abstract class UserStatusFilter {

    private UserStatusFilter next;

    public static UserStatusFilter link(UserStatusFilter first, UserStatusFilter... chain) {
        UserStatusFilter head = first;
        for (UserStatusFilter nextInChain : chain) {
            head.next = nextInChain;
            head = nextInChain;
        }
        return first;
    }

    public abstract boolean filter(User user);

    boolean checkNext(User user) {
        if (next == null) {
            return true;
        }
        return next.filter(user);
    }
}
