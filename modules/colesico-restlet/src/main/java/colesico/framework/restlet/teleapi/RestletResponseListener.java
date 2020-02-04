package colesico.framework.restlet.teleapi;

import colesico.framework.http.HttpContext;

@FunctionalInterface
public interface RestletResponseListener {
    void onResponse(HttpContext ctx, RestletDataPort dataPort);
}
