package colesico.framework.telehttp;

import colesico.framework.teleapi.dataport.TWContext;

import java.lang.reflect.Type;

/**
 * Basic tele-writing context for interaction via http
 */
abstract public class HttpTWContext<T extends Type, A> extends TWContext<T, A> {

    public HttpTWContext(T valueType, A attribute) {
        super(valueType, attribute);
    }

    public static <T extends Type, A> HttpTWContext<T, A> instance(T valueType) {
        return new HttpTWContext<>(valueType, null) {
        };
    }

    public static <T extends Type, A> HttpTWContext<T, A> instance(T valueType, A attribute) {
        return new HttpTWContext<>(valueType, attribute) {
        };
    }

}
