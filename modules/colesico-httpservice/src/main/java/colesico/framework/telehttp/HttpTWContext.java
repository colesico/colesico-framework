package colesico.framework.telehttp;

import colesico.framework.teleapi.dataport.TWContext;

import java.lang.reflect.Type;

/**
 * Basic tele-writing context for interaction via http
 */
abstract public class HttpTWContext<T extends Type, P> extends TWContext<T, P> {

    public HttpTWContext(T valueType, P payload) {
        super(valueType, payload);
    }

    public static <T extends Type, P> HttpTWContext<T, P> instance(T valueType) {
        return new HttpTWContext<>(valueType, null) {
        };
    }

    public static <T extends Type, P> HttpTWContext<T, P> instance(T valueType, P payload) {
        return new HttpTWContext<>(valueType, payload) {
        };
    }

}
