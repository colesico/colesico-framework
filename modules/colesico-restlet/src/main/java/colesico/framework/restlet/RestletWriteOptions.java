package colesico.framework.restlet;

import colesico.framework.telehttp.ContentType;
import colesico.framework.telehttp.TeleHttpWriteOptions;

import java.lang.reflect.Type;
import java.nio.charset.Charset;

/**
 *
 * @param customWriter Custom writer class or null. If null - default writer will be used.
 * @param metadata
 */
public record RestletWriteOptions(
        Type baseType,
        Integer statusCode,
        ContentType contentType,
        Charset charset,
        Class<? extends RestletTeleWriter<?>> customWriter,
        Object metadata
) implements TeleHttpWriteOptions {

    public static final String OF_METHOD = "of";

    public static RestletWriteOptions of() {
        return new RestletWriteOptions(
                null,
                null,
                null,
                null,
                null,
                null);
    }

    public static RestletWriteOptions of(Class<? extends RestletTeleWriter<?>> writerClass) {
        return new RestletWriteOptions(
                DEFAULT_SUCCESS_STATUS_CODE,
                DEFAULT_CONTENT_TYPE,
                DEFAULT_CHARSET,
                writerClass,
                null);
    }

    public static RestletWriteOptions of(Object attachment) {
        return new RestletWriteOptions(
                DEFAULT_SUCCESS_STATUS_CODE,
                DEFAULT_CONTENT_TYPE,
                DEFAULT_CHARSET,
                null,
                attachment);
    }

}
