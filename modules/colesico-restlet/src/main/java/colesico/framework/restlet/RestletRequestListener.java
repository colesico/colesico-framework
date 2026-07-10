package colesico.framework.restlet;

import colesico.framework.http.HttpContext;
import colesico.framework.httprouter.Router;


/**
 * To activate the listener, register it as an interface implementation in the IoC container
 * using {@link colesico.framework.ioc.production.Polyproduce}
 */
@FunctionalInterface
public interface RestletRequestListener {
    void onRequest(HttpContext ctx, RestletDataPort dataPort, Router.Invocation invocation);
}
