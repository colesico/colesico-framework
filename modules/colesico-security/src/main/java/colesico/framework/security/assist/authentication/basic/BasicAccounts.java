package colesico.framework.security.assist.authentication.basic;

import java.util.Set;

/**
 * Accounts storage API for basic authentication
 */
public interface BasicAccounts {

    default Account findAccount(String login, String passwordHashHex) {
        return null;
    }

    record Account(String login, Set<String> roles) {
    }
}
