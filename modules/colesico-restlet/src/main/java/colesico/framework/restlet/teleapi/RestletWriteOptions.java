package colesico.framework.restlet.teleapi;

import colesico.framework.telehttp.HttpWriteOptions;

import java.lang.reflect.Type;

/**
 *
 * @param writerClass Custom writer class or null. If null - default writer will be used.
 * @param attachment
 */
public record RestletWriteOptions(
        Class<? extends RestletTeleWriter<?>> writerClass,
        Object attachment
) implements HttpWriteOptions {

    public static final String OF_METHOD = "of";


    public static RestletWriteOptions of() {
        return new RestletWriteOptions(null, null);
    }

    public static RestletWriteOptions of(Class<? extends RestletTeleWriter<?>> writerClass) {
        return new RestletWriteOptions(writerClass, null);
    }

    public static RestletWriteOptions of(Object attachment) {
        return new RestletWriteOptions(null, attachment);
    }

}
