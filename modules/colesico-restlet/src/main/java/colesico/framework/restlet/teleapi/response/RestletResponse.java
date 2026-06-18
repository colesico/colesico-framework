package colesico.framework.restlet.teleapi.response;

import colesico.framework.telehttp.response.TeleHttpContentResponse;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * General purpose restlet response
 */
public class RestletResponse<T> extends TeleHttpContentResponse<T> {

    public static final String DEFAULT_CONTENT_TYPE = "application/json; charset=utf-8";
    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    protected final Charset charset;

    public RestletResponse(T content, String contentType, Integer statusCode, Charset charset) {
        super(content, contentType, statusCode);
        this.charset = charset;
    }

    public Charset charset() {
        return charset;
    }

    public static <T> RestletResponse<T> of(T content) {
        return new RestletResponse<>(content, DEFAULT_CONTENT_TYPE, 200, DEFAULT_CHARSET);
    }

    public static <T> RestletResponse<T> of(T content, int statusCode) {
        return new RestletResponse<>(content, DEFAULT_CONTENT_TYPE, statusCode, DEFAULT_CHARSET);
    }
}
