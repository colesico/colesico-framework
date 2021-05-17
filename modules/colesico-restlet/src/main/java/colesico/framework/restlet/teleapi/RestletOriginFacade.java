package colesico.framework.restlet.teleapi;

import colesico.framework.http.HttpRequest;
import colesico.framework.router.RouterContext;
import colesico.framework.telehttp.Origin;
import colesico.framework.telehttp.OriginFacade;

abstract public class RestletOriginFacade extends OriginFacade {

    public static final OriginFacade FIELD = new OriginFacade() {
        @Override
        public Origin getOrigin() {
            return RestletOrigin.ORIGIN_FIELD;
        }

        @Override
        public String getString(String name, RouterContext routerContext, HttpRequest httpRequest) {
            return httpRequest.getQueryParameters().get(name);
        }

    };

}
