package colesico.framework.weblet;

import colesico.framework.weblet.teleapi.ForwardResponse;

/**
 * Dynamically constructed response
 */
public final class WebletResponse {

    private final Object response;

    private WebletResponse(Object response) {
        this.response = response;
    }

    public Object getResponse() {
        return response;
    }

    public static WebletResponse of(Object response) {
        return new WebletResponse(response);
    }

    public static WebletResponse of(RedirectResponse response) {
        return new WebletResponse(response);
    }

    public static WebletResponse of(ForwardResponse response) {
        return new WebletResponse(response);
    }

    public static WebletResponse of(ViewResponse response) {
        return new WebletResponse(response);
    }

    public static WebletResponse of(HtmlResponse response) {
        return new WebletResponse(response);
    }

    public static WebletResponse of(BinaryResponse response) {
        return new WebletResponse(response);
    }

    public static WebletResponse of(TextResponse response) {
        return new WebletResponse(response);
    }
}
