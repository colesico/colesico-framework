package colesico.framework.weblet.response;

import colesico.framework.http.HttpCookie;
import colesico.framework.telehttp.ContentType;
import colesico.framework.telehttp.response.TeleHttpResponse;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Returns  model that be presented to given view
 */
public final class ViewResponse extends TeleHttpResponse {

    private final String view;
    private final Object model;

    public ViewResponse(Integer statusCode, ContentType contentType, Map<String, List<String>> headers, Set<HttpCookie> cookies, String view, Object model) {
        super(statusCode, contentType, headers, cookies);
        this.view = view;
        this.model = model;
    }

    public String view() {
        return view;
    }

    public Object model() {
        return model;
    }

    public static ViewResponse.Builder view(String view) {
        return new ViewResponse.Builder(view);
    }

    public static class Builder extends TeleHttpResponse.Builder<ViewResponse, ViewResponse.Builder> {

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
        public ViewResponse build() {
            return new ViewResponse(statusCode, contentType, headers, cookies, viewName, model);
        }
    }
}
