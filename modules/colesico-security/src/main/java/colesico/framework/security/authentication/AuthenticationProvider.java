package colesico.framework.security.authentication;

import colesico.framework.security.Principal;
import colesico.framework.security.SecurityManager;

import java.util.Optional;

public interface AuthenticationProvider<P extends Principal<?>, C extends SecurityManager.Credentials> {

    /**
     * Authenticate user by credentials
     */
    Optional<P> authenticate(C credentials);
}
