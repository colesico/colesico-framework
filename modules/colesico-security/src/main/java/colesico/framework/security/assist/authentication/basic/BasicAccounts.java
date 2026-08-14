package colesico.framework.security.assist.authentication.basic;

import java.util.Set;

public interface BasicAccounts {

    default Account findAccount(String login, String passwordHashHex) {
        return null;
    }

    record Account(String login, Set<String> roles) {
    }
}
