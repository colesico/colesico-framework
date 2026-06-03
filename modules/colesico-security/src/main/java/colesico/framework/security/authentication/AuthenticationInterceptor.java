package colesico.framework.security.authentication;

import colesico.framework.service.interception.Interceptor;

@FunctionalInterface
public interface AuthenticationInterceptor<T,R> extends Interceptor<T,R> {
    record Options(Class<AuthenticationSource>[] sources, boolean login){

    }
}
