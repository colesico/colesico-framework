package colesico.framework.weblet.result;

import colesico.framework.http.HttpCookie;
import colesico.framework.telehttp.ContentType;
import colesico.framework.telehttp.result.AbstractHttpResult;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Returns  model that be presented to given view
 */
public final class ViewResult extends AbstractHttpResult {

    private final String view;
    private final Object model;

    public ViewResult(Integer status, ContentType contentType, Map<String, List<String>> headers, Set<HttpCookie> cookies, String view, Object model) {
        super(status, contentType, headers, cookies);
        this.view = view;
        this.model = model;
    }

    public String view() {
        return view;
    }

    public Object model() {
        return model;
    }

    public static ViewResult.Builder view(String view) {
        return new ViewResult.Builder(view);
    }

    public static class Builder extends AbstractHttpResult.Builder<ViewResult, ViewResult.Builder> {

        protected final String viewName;
        protected Object model;

        public Builder(String viewName) {
            this.viewName = viewName;
        }

        public Builder model(Object model) {
            this.model = model;
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public ViewResult build() {
            return new ViewResult(status, contentType, headers, cookies, viewName, model);
        }
    }
}
