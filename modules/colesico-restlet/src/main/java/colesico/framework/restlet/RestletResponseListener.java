package colesico.framework.restlet;

import colesico.framework.http.HttpContext;

/**
 * To activate the listener, register it as an interface implementation in the IoC container
 * using {@link colesico.framework.ioc.production.Polyproduce}
 */
@FunctionalInterface
public interface RestletResponseListener {
    void onResponse(HttpContext ctx, RestletDataPort dataPort);
}
