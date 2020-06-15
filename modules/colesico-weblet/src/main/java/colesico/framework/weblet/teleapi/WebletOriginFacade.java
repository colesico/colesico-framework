package colesico.framework.weblet.teleapi;

import colesico.framework.http.HttpMethod;
import colesico.framework.http.HttpRequest;
import colesico.framework.router.RouterContext;
import colesico.framework.weblet.Origin;

/**
 * Weblet specific AUTO origin facade
 */
public interface WebletOriginFacade extends OriginFacade {

    OriginFacade AUTO = new OriginFacade() {

        @Override
        public Origin getOrigin() {
            return Origin.AUTO;
        }

        @Override
        public String getString(String name, RouterContext routerContext, HttpRequest httpRequest) {
            String value = null;
            switch (httpRequest.getRequestMethod().getName()) {
                case HttpMethod.GET:
                case HttpMethod.HEAD:
                    value = httpRequest.getQueryParameters().get(name);
                    if (value != null) {
                        return value;
                    }
                    return routerContext.getParameters().get(name);
                case HttpMethod.POST:
                case HttpMethod.PUT:
                case HttpMethod.PATCH:
                    value = httpRequest.getPostParameters().get(name);
                    if (value != null) {
                        return value;
                    }
                    value = httpRequest.getQueryParameters().get(name);
                    if (value != null) {
                        return value;
                    }
                    return routerContext.getParameters().get(name);
                default:
                    return value;
            }
        }
    };

}
