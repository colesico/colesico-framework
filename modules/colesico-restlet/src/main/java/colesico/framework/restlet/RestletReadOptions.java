package colesico.framework.restlet;

import colesico.framework.telehttp.TeleHttpReadOptions;

import java.lang.reflect.Type;

/**
 *
 * @param paramName
 * @param originName
 * @param customReader Custom reader class or null. If null - default reader will be used to  read the parameter
 * @param metadata
 */
public record RestletReadOptions(
        Type baseType,
        String paramName,
        String originName,
        Class<? extends RestletTeleReader<?>> customReader,
        Object metadata
) implements TeleHttpReadOptions {

    public static final String OF_METHOD = "of";

    public static RestletReadOptions of() {
        return new RestletReadOptions(null, null, RestletOrigin.AUTO, null, null);
    }

    public static RestletReadOptions of(Type baseType) {
        return new RestletReadOptions(baseType, null, RestletOrigin.AUTO, null, null);
    }

    public static RestletReadOptions of(Type baseType, String paramName) {
        return new RestletReadOptions(baseType, paramName, RestletOrigin.AUTO, null, null);
    }

    public static RestletReadOptions of(Type baseType, String paramName, String originName) {
        return new RestletReadOptions(baseType, paramName, originName, null, null);
    }

    public static RestletReadOptions of(Type baseType, String paramName, String originName, Class<? extends RestletTeleReader<?>> readerClass) {
        return new RestletReadOptions(baseType, paramName, originName, readerClass, null);
    }

    /**
     * For manual usage
     */
    public static RestletReadOptions of(Type baseType, Object metadata) {
        return new RestletReadOptions(baseType, null, RestletOrigin.AUTO, null, metadata);
    }
}
