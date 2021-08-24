package colesico.framework.restlet.teleapi;

import colesico.framework.telehttp.HttpTRContext;

import java.lang.reflect.Type;

/**
 * @param <R> Json request type
 * @param <V> Json request value type
 */
public final class RestletTRContext<R, V> extends HttpTRContext {

    public static final String OF_METHOD = "of";

    /**
     * Custom reader class or null.
     * If null - default reader will be used to read the parameter
     */
    private final Class<? extends RestletTeleReader> readerClass;

    /**
     * Json request field getter
     *
     * @see colesico.framework.restlet.teleapi.jsonrequest.JsonField
     */
    private final JsonFieldGetter<R, V> jsonFieldGetter;

    private RestletTRContext(Type valueType, String paramName, String originName, Class<? extends RestletTeleReader> readerClass, JsonFieldGetter<R, V> jsonFieldGetter) {
        super(valueType, paramName, originName);
        this.readerClass = readerClass;
        this.jsonFieldGetter = jsonFieldGetter;
    }

    public Class<? extends RestletTeleReader> getReaderClass() {
        return readerClass;
    }

    public JsonFieldGetter<R, V> getJsonFieldGetter() {
        return jsonFieldGetter;
    }

    public static <R, V> RestletTRContext<R, V> of(Type valueType, String paramName, String originName, Class<? extends RestletTeleReader> readerClass, JsonFieldGetter<R, V> fieldGetter) {
        return new RestletTRContext<>(valueType, paramName, originName, readerClass, fieldGetter);
    }

    public static <R, V> RestletTRContext<R, V> of(Type valueType, String paramName, String originName, Class<? extends RestletTeleReader> readerClass) {
        return new RestletTRContext<>(valueType, paramName, originName, readerClass, null);
    }

    public static <R, V> RestletTRContext<R, V> of(Type valueType, String paramName, String originName) {
        return new RestletTRContext<>(valueType, paramName, originName, null, null);
    }

    public static <R, V> RestletTRContext<R, V> of(Type valueType, String paramName) {
        return new RestletTRContext<>(valueType, paramName, RestletOrigin.AUTO, null, null);
    }

    public static <R, V> RestletTRContext<R, V> of(Type valueType) {
        return new RestletTRContext<>(valueType, null, RestletOrigin.AUTO, null, null);
    }

    @FunctionalInterface
    public interface JsonFieldGetter<R, V> {
        V get(R jsonRequest);
    }
}
