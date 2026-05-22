package colesico.framework.telehttp;

import colesico.framework.teleapi.dataport.WriteOptions;

import java.lang.reflect.Type;

/**
 * Basic tele-writing context for interaction via http
 */
abstract public class HttpWriteOptions<T extends Type, P> extends WriteOptions<T, P> {

    public HttpWriteOptions(T valueType, P payload) {
        super(valueType, payload);
    }
}
