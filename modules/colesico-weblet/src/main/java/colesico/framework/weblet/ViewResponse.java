package colesico.framework.weblet;

/**
 * Returns  model that be presented to given view
 */
public final class ViewResponse extends ContentResponse {

    private final String viewName;
    private final Object model;

    private ViewResponse(String viewName, Object model, String contentType, int statusCode) {
        super(contentType, statusCode);
        this.viewName = viewName;
        this.model = model;
    }

    public static ViewResponse of(String viewName) {
        return new ViewResponse(viewName, null, null, DEFAULT_STATUS_CODE);
    }

    public static ViewResponse of(String viewName, Object model) {
        return new ViewResponse(viewName, model, null, DEFAULT_STATUS_CODE);
    }

    public static ViewResponse of(String viewName, int statusCode) {
        return new ViewResponse(viewName, null, null, statusCode);
    }

    public static ViewResponse of(String viewName, Object model, int statusCode) {
        return new ViewResponse(viewName, model, null, statusCode);
    }

    public static ViewResponse of(String viewName, Object model, String contentType, int statusCode) {
        return new ViewResponse(viewName, model, contentType, statusCode);
    }

    public WebletResponse wrap() {
        return WebletResponse.of(this);
    }

    public String getViewName() {
        return viewName;
    }

    public Object getModel() {
        return model;
    }

}
