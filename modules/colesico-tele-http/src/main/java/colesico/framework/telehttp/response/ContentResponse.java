package colesico.framework.telehttp.response;

import colesico.framework.telehttp.MediaType;

/**
 * Response model with content
 */
public class ContentResponse<C> extends TeleHttpResponse {

    protected final C content;

    public ContentResponse(Integer statusCode, MediaType mediaType, C content) {
        super(statusCode, mediaType);
        this.content = content;
    }

    public C content() {
        return content;
    }

    public static <C> ContentResponse<C> of(C content) {
        return new ContentResponse<>(null, null, content);
    }

    public static <C> ContentResponse<C> of(MediaType mediaType, C content) {
        return new ContentResponse<>(null, mediaType, content);
    }

    public static <C> ContentResponse<C> of(Integer statusCode, C content) {
        return new ContentResponse<>(statusCode, null, content);
    }

    public static <C> ContentResponse<C> of(Integer statusCode, MediaType mediaType, C content) {
        return new ContentResponse<>(statusCode, mediaType, content);
    }
}
