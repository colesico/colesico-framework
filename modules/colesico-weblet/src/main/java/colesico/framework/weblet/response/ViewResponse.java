package colesico.framework.weblet.response;

import colesico.framework.telehttp.response.TeleHttpResponse;
import colesico.framework.weblet.teleapi.WebletWriteOptions;

/**
 * Returns  model that be presented to given view
 */
public final class ViewResponse extends TeleHttpResponse {

    public static final String DEFAULT_CONTENT_TYPE = "text/html; charset=utf-8";

    private final String viewName;
    private final Object model;

    public ViewResponse(Integer statusCode, String contentType, String viewName, Object model) {
        super(statusCode, contentType);
        this.viewName = viewName;
        this.model = model;
    }

    public static ViewResponse of(String viewName) {
        return new ViewResponse(
                WebletWriteOptions.DEFAULT_STATUS_CODE,
                DEFAULT_CONTENT_TYPE,
                viewName,
                null
        );
    }

    public static ViewResponse of(String viewName, Object model) {
        return new ViewResponse(
                WebletWriteOptions.DEFAULT_STATUS_CODE,
                DEFAULT_CONTENT_TYPE,
                viewName,
                model
        );
    }

    public static ViewResponse of(int statusCode, String viewName, Object model) {
        return new ViewResponse(
                statusCode,
                DEFAULT_CONTENT_TYPE,
                viewName,
                model
        );
    }

    public String viewName() {
        return viewName;
    }

    public Object model() {
        return model;
    }

    public WebletResponse wrap() {
        return WebletResponse.of(this);
    }

}
