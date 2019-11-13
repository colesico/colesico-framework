package colesico.framework.undertow.internal;

import colesico.framework.http.HttpRequest;
import colesico.framework.http.HttpResponse;
import colesico.framework.httpserver.ErrorHandler;
import colesico.framework.httpserver.RequestProcessor;
import colesico.framework.ioc.ThreadScope;
import colesico.framework.router.Router;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UndertowRequestProcessor extends RequestProcessor<HttpServerExchange> implements HttpHandler {

    @Inject
    public UndertowRequestProcessor(ThreadScope threadScope, Router router, ErrorHandler errorHandler) {
        super(threadScope, router, errorHandler);
    }

    @Override
    protected HttpRequest createHttpRequest(HttpServerExchange exchange) {
        return new HttpRequestImpl(exchange);
    }

    @Override
    protected HttpResponse createHttpResponse(HttpServerExchange exchange) {
        return new HttpResponseImpl(exchange);
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        //TODO: use configuration run in blocking thread with url mapping?
        exchange.startBlocking();
        if (exchange.isInIoThread()) {
            exchange.dispatch(this);
            return;
        }
        processRequest(exchange);
    }
}
