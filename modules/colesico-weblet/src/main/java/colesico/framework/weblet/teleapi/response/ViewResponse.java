package colesico.framework.weblet.teleapi.response;

import colesico.framework.telehttp.response.TeleHttpResponse;

/**
 * Returns  model that be presented to given view
 */
public final class ViewResponse extends TeleHttpResponse {

    private final String viewName;
    private final Object model;

    public ViewResponse(Integer statusCode, String contentType, String viewName, Object model) {
        super(statusCode, contentType);
        this.viewName = viewName;
        this.model = model;
    }

    public static ViewResponse of(String viewName) {
        return new ViewResponse(
                200,
                null,
                viewName,
                null
        );
    }

    public static ViewResponse of(String viewName, Object model) {
        return new ViewResponse(
                200,
                null,
                viewName,
                model
        );
    }

    public static ViewResponse of(int statusCode, String viewName, Object model) {
        return new ViewResponse(
                statusCode,
                null,
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

 }
