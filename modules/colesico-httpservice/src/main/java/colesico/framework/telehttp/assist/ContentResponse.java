package colesico.framework.telehttp.assist;


abstract public class ContentResponse<C> extends TeleHttpResponse {

    protected final C content;

    public ContentResponse(C content, String contentType, int statusCode) {
        super(contentType, statusCode);
        this.content = content;
    }

    public C content() {
        return content;
    }

}
