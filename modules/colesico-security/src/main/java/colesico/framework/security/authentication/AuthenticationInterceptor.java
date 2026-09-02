package colesico.framework.security.authentication;

import colesico.framework.service.interception.Interceptor;

import java.util.Map;

@FunctionalInterface
public interface AuthenticationInterceptor extends Interceptor<AuthenticationInterceptor.Options> {
    record Options(AuthenticatorSpec[] authenticators,
                   AuthenticationPolicy.Strategy strategy) {

    }

    record AuthenticatorSpec(
            Class<? extends Authenticator<?, ?>> authenticatorClass,
            Class<?> classed,
            Map<String, String> properties
    ) {
        public static final String OF_METHOD = "of";
        
        public static AuthenticatorSpec of(Class<? extends Authenticator<?, ?>> authenticatorClass,
                                           Class<?> classed,
                                           Map<String, String> properties
        ) {
            return new AuthenticatorSpec(authenticatorClass, classed, properties);
        }
    }
}
