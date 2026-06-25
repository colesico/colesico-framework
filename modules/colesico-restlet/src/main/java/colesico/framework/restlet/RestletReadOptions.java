package colesico.framework.restlet;

import colesico.framework.telehttp.TeleHttpReadOptions;

/**
 *
 * @param paramName
 * @param originName
 * @param readerClass Custom reader class or null. If null - default reader will be used to  read the parameter
 * @param attachment
 */
public record RestletReadOptions(
        String paramName,
        String originName,
        Class<? extends RestletTeleReader<?>> readerClass,
        Object attachment
) implements TeleHttpReadOptions {

    public static final String OF_METHOD = "of";

    public static RestletReadOptions of() {
        return new RestletReadOptions(null, RestletOrigin.AUTO, null, null);
    }

    public static RestletReadOptions of(Object attachment) {
        return new RestletReadOptions(null, RestletOrigin.AUTO, null, attachment);
    }

    public static RestletReadOptions of(String paramName) {
        return new RestletReadOptions(paramName, RestletOrigin.AUTO, null, null);
    }

    public static RestletReadOptions of(String paramName, String originName) {
        return new RestletReadOptions(paramName, originName, null, null);
    }

    public static RestletReadOptions of(String paramName, String originName, Class<? extends RestletTeleReader<?>> readerClass) {
        return new RestletReadOptions(paramName, originName, readerClass, null);
    }

}
