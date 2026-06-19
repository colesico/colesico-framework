package colesico.framework.telehttp.response;


abstract public class TeleHttpContentResponse<C> extends TeleHttpResponse {

    protected final C content;

    public TeleHttpContentResponse(Integer statusCode, String contentType, C content) {
        super(statusCode, contentType);
        this.content = content;
    }

    public C content() {
        return content;
    }

}
