package colesico.framework.telehttp;

import colesico.framework.http.HttpCookie;
import colesico.framework.teleapi.dataport.TWContext;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Basic tele-writing context for interaction via http
 */
abstract public class HttpTWContext<T extends Type, P> extends TWContext<T, P> {

    public HttpTWContext(T valueType, P payload) {
        super(valueType, payload);
    }
}
