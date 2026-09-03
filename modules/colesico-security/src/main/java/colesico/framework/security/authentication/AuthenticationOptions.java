package colesico.framework.security.authentication;

import colesico.framework.security.Identity;
import colesico.framework.security.IdentityContext;
import colesico.framework.security.SecurityManager;

import java.lang.annotation.*;

@Documented
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface AuthenticationOptions {

    /**
     * Determines the execution strategy for authentication.
     */
    Strategy strategy();

    /**
     * Custom authentication result handler
     */
    Class<? extends AuthenticationResultHandler> resultHandler() default AuthenticationResultHandler.class;

    /**
     * Strategies defining how and when authentication is triggered.
     */
    enum Strategy {

        /**
         * Always performs authentication (call {@link colesico.framework.security.SecurityManager#authenticate(Authenticator)}).
         */
        STRICT,

        /**
         * Performs authentication only if an {@link Identity} is missing from the {@link IdentityContext}.
         */
        IF_NECESSARY,

        /**
         * Only registers authenticators in the {@link AuthenticationContext} for manual authentication
         * by calling {@link SecurityManager#authenticate()} later within the business logic.
         */
        DEFERRED
    }
}
