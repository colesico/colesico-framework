package colesico.framework.security.authentication;

import colesico.framework.security.Principal;
import colesico.framework.security.SecurityManager;

public interface AuthenticationManager<P extends Principal<?>> {
    P authenticate(SecurityManager.Credentials credentials);
}
