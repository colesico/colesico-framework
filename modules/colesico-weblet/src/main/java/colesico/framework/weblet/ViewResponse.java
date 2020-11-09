package colesico.framework.weblet;

/**
 * Returns  model that be presented to given view
 */
public final class ViewResponse {

    private final String vewName;
    private final Object model;

    private ViewResponse(String vewName, Object model) {
        this.vewName = vewName;
        this.model = model;
    }

    public static ViewResponse of(String viewName, Object model) {
        return new ViewResponse(viewName, model);
    }

    public String getVewName() {
        return vewName;
    }

    public Object getModel() {
        return model;
    }
}
