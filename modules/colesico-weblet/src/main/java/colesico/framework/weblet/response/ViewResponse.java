package colesico.framework.weblet.response;

import colesico.framework.telehttp.response.HttpTeleResponse;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Returns  model that be presented to given view
 */
public final class ViewResponse extends HttpTeleResponse {

    public static final Integer DEFAULT_STATUS_CODE = 200;
    public static final String DEFAULT_CONTENT_TYPE = "text/html; charset=utf-8";
    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    private final String viewName;
    private final Object model;

    public ViewResponse(Integer statusCode, String contentType, String viewName, Object model) {
        super(statusCode, contentType);
        this.viewName = viewName;
        this.model = model;
    }

    public static ViewResponse of(String viewName) {
        return new ViewResponse(
                DEFAULT_STATUS_CODE,
                DEFAULT_CONTENT_TYPE,
                viewName,
                null
        );
    }

    public static ViewResponse of(String viewName, Object model) {
        return new ViewResponse(
                DEFAULT_STATUS_CODE,
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

    public DynamicResponse wrap() {
        return DynamicResponse.of(this);
    }

}
