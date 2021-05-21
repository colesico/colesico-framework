package colesico.framework.restlet.teleapi;

import colesico.framework.telehttp.HttpTRContext;
import colesico.framework.telehttp.Origin;

public final class RestletTRContext<M, V> extends HttpTRContext {

    public static final String OF_METHOD = "of";

    /**
     * Custom reader class or null.
     * If null - default reader will be used to read the parameter
     */
    private final Class<? extends RestletTeleReader> readerClass;

    /**
     * Json map entry getter
     *
     * @see colesico.framework.restlet.teleapi.jsonrequest.JsonField
     */
    private final JsonFieldGetter<M, V> fieldGetter;

    private RestletTRContext(String paramName, String originName, Class<? extends RestletTeleReader> readerClass, JsonFieldGetter<M, V> fieldGetter) {
        super(paramName, originName);
        this.readerClass = readerClass;
        this.fieldGetter = fieldGetter;
    }

    public Class<? extends RestletTeleReader> getReaderClass() {
        return readerClass;
    }

    public JsonFieldGetter<M, V> getFieldGetter() {
        return fieldGetter;
    }

    public static <M, V> RestletTRContext<M, V> of(String paramName, String originName, Class<? extends RestletTeleReader> readerClass, JsonFieldGetter<M, V> fieldGetter) {
        return new RestletTRContext<>(paramName, originName, readerClass, fieldGetter);
    }

    public static <M, V> RestletTRContext<M, V> of(String paramName, String originName, Class<? extends RestletTeleReader> readerClass) {
        return new RestletTRContext<>(paramName, originName, readerClass, null);
    }

    public static <M, V> RestletTRContext<M, V> of(String paramName, String originName) {
        return new RestletTRContext<>(paramName, originName, null, null);
    }

    public static <M, V> RestletTRContext<M, V> of(String paramName) {
        return new RestletTRContext<>(paramName, Origin.AUTO, null, null);
    }

    public static <M, V> RestletTRContext<M, V> of() {
        return new RestletTRContext<>(null, Origin.AUTO, null, null);
    }

    @FunctionalInterface
    public interface JsonFieldGetter<M, V> {
        V get(M jsonMap);
    }
}
