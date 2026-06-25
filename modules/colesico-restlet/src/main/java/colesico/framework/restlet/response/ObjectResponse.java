package colesico.framework.restlet.response;

import colesico.framework.telehttp.response.ContentResponse;

import java.nio.charset.Charset;

/**
 * General purpose restlet value
 */
public class ObjectResponse<T> extends ContentResponse<T> {

    protected final Charset charset;

    public ObjectResponse(Integer statusCode, String contentType, T content, Charset charset) {
        super(statusCode, contentType, content);
        this.charset = charset;
    }

    public static <T> ObjectResponse<T> of(T content) {
        return new ObjectResponse<>(
                null,
                null,
                content,
                null
        );
    }

    public static <T> ObjectResponse<T> of(int statusCode, T content) {
        return new ObjectResponse<>(
                statusCode,
                null,
                content,
                null
        );
    }

    public static <T> ObjectResponse<T> of(int statusCode, String contentType, T content) {
        return new ObjectResponse<>(
                statusCode,
                contentType,
                content,
                null
        );
    }

    public Charset charset() {
        return charset;
    }
}
