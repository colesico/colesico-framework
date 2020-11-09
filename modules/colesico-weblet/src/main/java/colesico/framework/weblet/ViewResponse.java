package colesico.framework.weblet;

/**
 * Returns  model that be presented to given view
 */
public final class ViewResponse {

    public static final int DEFAULT_HTTP_CODE = 200;

    private final String vewName;
    private final Object model;
    protected final int httpCode;

    private ViewResponse(String vewName, Object model, int httpCode) {
        this.vewName = vewName;
        this.model = model;
        this.httpCode = httpCode;
    }

    public static ViewResponse of(String viewName) {
        return new ViewResponse(viewName, null, DEFAULT_HTTP_CODE);
    }

    public static ViewResponse of(String viewName, Object model) {
        return new ViewResponse(viewName, model, DEFAULT_HTTP_CODE);
    }

    public static ViewResponse of(String viewName, Object model, int httpCode) {
        return new ViewResponse(viewName, model, httpCode);
    }

    public String getVewName() {
        return vewName;
    }

    public Object getModel() {
        return model;
    }

    public int getHttpCode() {
        return httpCode;
    }
}
