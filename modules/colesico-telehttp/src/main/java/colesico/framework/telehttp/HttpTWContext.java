package colesico.framework.telehttp;

import colesico.framework.teleapi.TWContext;

import java.lang.reflect.Type;

/**
 * Basic tele-writing context for interaction via http
 */
abstract public class HttpTWContext extends TWContext {

    protected HttpTWContext(Type valueType) {
        super(valueType);
    }

    public static HttpTWContext instance(Type valueType) {
        return new HttpTWContext(valueType) {
        };
    }
}
