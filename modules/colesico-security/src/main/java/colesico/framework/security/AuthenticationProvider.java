package colesico.framework.security;

import java.util.Optional;

public interface AuthenticationProvider<P extends Principal<?>, C extends SecurityManager.Credentials> {

    /**
     * Authenticate user by credentials
     */
    Optional<P> authenticate(C credentials);
}
