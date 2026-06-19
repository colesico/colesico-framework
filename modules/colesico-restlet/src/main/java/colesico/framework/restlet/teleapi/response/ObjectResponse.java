package colesico.framework.restlet.teleapi.response;

import colesico.framework.restlet.teleapi.RestletWriteOptions;
import colesico.framework.telehttp.response.TeleHttpContentResponse;

import java.nio.charset.Charset;

/**
 * General purpose restlet response
 */
public class ObjectResponse<T> extends TeleHttpContentResponse<T> {

    protected final Charset charset;

    public ObjectResponse(Integer statusCode, String contentType, T content, Charset charset) {
        super(statusCode, contentType, content);
        this.charset = charset;
    }

    public static <T> ObjectResponse<T> of(T content) {
        return new ObjectResponse<>(
                RestletWriteOptions.DEFAULT_STATUS_CODE,
                RestletWriteOptions.DEFAULT_CONTENT_TYPE,
                content,
                RestletWriteOptions.DEFAULT_CHARSET
        );
    }

    public static <T> ObjectResponse<T> of(int statusCode, T content) {
        return new ObjectResponse<>(
                statusCode,
                RestletWriteOptions.DEFAULT_CONTENT_TYPE,
                content,
                RestletWriteOptions.DEFAULT_CHARSET
        );
    }

    public static <T> ObjectResponse<T> of(int statusCode, String contentType, T content) {
        return new ObjectResponse<>(
                statusCode,
                contentType,
                content,
                RestletWriteOptions.DEFAULT_CHARSET
        );
    }

    public Charset charset() {
        return charset;
    }
}
