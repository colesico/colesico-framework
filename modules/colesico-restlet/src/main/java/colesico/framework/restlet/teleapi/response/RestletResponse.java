package colesico.framework.restlet.teleapi.response;

import colesico.framework.telehttp.response.TeleHttpContentResponse;

import java.nio.charset.Charset;

/**
 * General purpose restlet response
 */
public class RestletResponse<T> extends TeleHttpContentResponse<T> {

    protected Charset charset;

    public RestletResponse(T content, String contentType, Integer statusCode) {
        super(content, contentType, statusCode);
    }

    public Charset charset() {
        return charset;
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    public static <T> RestletResponse<T> of(T content) {
        return new RestletResponse<>(content, null, 200);
    }

    public static <T> RestletResponse<T> of(T content, int statusCode) {
        return new RestletResponse<>(content, null, statusCode);
    }
}
