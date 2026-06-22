package colesico.framework.restlet.teleapi;

import colesico.framework.telehttp.HttpWriteOptions;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 *
 * @param writerClass Custom writer class or null. If null - default writer will be used.
 * @param attachment
 */
public record RestletWriteOptions(
        Integer statusCode,
        String contentType,
        Charset charset,
        Class<? extends RestletTeleWriter<?>> writerClass,
        Object attachment
) implements HttpWriteOptions {

    public static final Integer DEFAULT_SUCCESS_STATUS_CODE = 200;
    public static final Integer DEFAULT_ERROR_STATUS_CODE = 500;
    public static final String DEFAULT_CONTENT_TYPE = "application/json; charset=utf-8";
    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    public static final String OF_METHOD = "of";

    public RestletWriteOptions(Integer statusCode, String contentType, Charset charset, Class<? extends RestletTeleWriter<?>> writerClass, Object attachment) {
        this.statusCode = statusCode;
        this.contentType = contentType;
        this.charset = charset;
        this.writerClass = writerClass;
        this.attachment = attachment;
    }

    public static RestletWriteOptions of() {
        return new RestletWriteOptions(
                DEFAULT_SUCCESS_STATUS_CODE,
                DEFAULT_CONTENT_TYPE,
                DEFAULT_CHARSET,
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
