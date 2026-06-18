package colesico.framework.telehttp.response;


abstract public class TeleHttpContentResponse<C> extends TeleHttpResponse {

    protected final C content;

    public TeleHttpContentResponse(C content, String contentType, int statusCode) {
        super(contentType, statusCode);
        this.content = content;
    }

    public C content() {
        return content;
    }

}
