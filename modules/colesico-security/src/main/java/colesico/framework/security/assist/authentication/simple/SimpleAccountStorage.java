package colesico.framework.security.assist.authentication.simple;

import java.util.Set;

public interface SimpleAccountStorage {

    default Account findAccount(String login, String passwordHashHex) {
        return null;
    }

    record Account(String login, Set<String> roles) {
    }
}
