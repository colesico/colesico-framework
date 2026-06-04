package colesico.framework.security.authentication;

import colesico.framework.service.interception.Interceptor;

@FunctionalInterface
public interface AuthenticationInterceptor extends Interceptor<AuthenticationInterceptor.Options> {
    record Options(Class<? extends AuthenticationSource<?, ?>>[] sources, boolean login) {

    }
}
