package colesico.framework.restlet.teleapi;

import colesico.framework.http.HttpContext;

@FunctionalInterface
public interface RestletRequestListener {
    void onRequest(HttpContext ctx, RestletDataPort dataPort, Object service);
}
