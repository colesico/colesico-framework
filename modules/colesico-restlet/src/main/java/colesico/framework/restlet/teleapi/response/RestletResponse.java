package colesico.framework.restlet.teleapi.response;

import colesico.framework.restlet.teleapi.RestletWriteOptions;
import colesico.framework.telehttp.response.TeleHttpContentResponse;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * General purpose restlet response
 */
public class RestletResponse<T> extends TeleHttpContentResponse<T> {

    protected final Charset charset;

    public RestletResponse(Integer statusCode, String contentType, T content, Charset charset) {
        super(statusCode, contentType, content);
        this.charset = charset;
    }

    public Charset charset() {
        return charset;
    }

    public static <T> RestletResponse<T> of(T content) {
        return new RestletResponse<>(
                RestletWriteOptions.DEFAULT_STATUS_CODE,
                RestletWriteOptions.DEFAULT_CONTENT_TYPE,
                content,
                RestletWriteOptions.DEFAULT_CHARSET
        );
    }

    public static <T> RestletResponse<T> of(int statusCode, T content) {
        return new RestletResponse<>(
                statusCode,
                RestletWriteOptions.DEFAULT_CONTENT_TYPE,
                content,
                RestletWriteOptions.DEFAULT_CHARSET
        );
    }

    public static <T> RestletResponse<T> of(int statusCode, String contentType, T content) {
        return new RestletResponse<>(
                statusCode,
                contentType,
                content,
                RestletWriteOptions.DEFAULT_CHARSET
        );
    }
}
