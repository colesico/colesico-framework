package colesico.framework.telehttp.response;

/**
 * Response model with content
 */
public class ContentResponse<C> extends TeleHttpResponse {

    protected final C content;

    public ContentResponse(Integer statusCode, String contentType, C content) {
        super(statusCode, contentType);
        this.content = content;
    }

    public C content() {
        return content;
    }

    public static <C> ContentResponse<C> of(C content) {
        return new ContentResponse<>(null, null, content);
    }
}
