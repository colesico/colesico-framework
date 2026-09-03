package colesico.framework.security.authentication;

import colesico.framework.service.interception.Interceptor;

@FunctionalInterface
public interface AuthenticationInterceptor extends Interceptor<AuthenticationInterceptor.Options> {
    record Options(AuthenticatorSpec[] authenticators,
                   AuthenticationOptions.Strategy strategy,
                   Class<? extends AuthenticationResultHandler> resultHandlerClass) {

    }

    record AuthenticatorSpec(
            Class<? extends Authenticator<?, ?>> authenticatorClass,
            Class<?> classed
    ) {
        public static final String OF_METHOD = "of";

        public static AuthenticatorSpec of(Class<? extends Authenticator<?, ?>> authenticatorClass,
                                           Class<?> classed
        ) {
            return new AuthenticatorSpec(authenticatorClass, classed);
        }
    }
}
