package colesico.framework.undertow.internal;

import colesico.framework.http.HttpRequest;
import colesico.framework.http.HttpResponse;
import colesico.framework.httpserver.ErrorHandler;
import colesico.framework.httpserver.RequestProcessor;
import colesico.framework.ioc.message.Message;
import colesico.framework.ioc.scope.ThreadScope;
import colesico.framework.ioc.scope.Unscoped;
import colesico.framework.router.ActionResolution;
import colesico.framework.router.Router;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

import javax.inject.Inject;

/**
 * This processor is used to perform blocking requests processing and
 * dispatched from UndertowRequestProcessor
 */
@Unscoped
public class UndertowBlockingHandler extends RequestProcessor<HttpServerExchange> implements HttpHandler {

    private final ActionResolution resolution;

    @Inject
    public UndertowBlockingHandler(@Message ActionResolution resolution, ThreadScope threadScope, Router router, ErrorHandler errorHandler) {
        super(threadScope, router, errorHandler);
        this.resolution = resolution;
    }

    @Override
    protected HttpRequest createHttpRequest(HttpServerExchange exchange) {
        return new UndertowHttpRequest(exchange);
    }

    @Override
    protected HttpResponse createHttpResponse(HttpServerExchange exchange) {
        return new UndertowHttpResponse(exchange);
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        performAction(resolution,exchange);
        exchange.endExchange();
    }
}
