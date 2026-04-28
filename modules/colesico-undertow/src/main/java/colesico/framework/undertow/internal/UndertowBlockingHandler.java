package colesico.framework.undertow.internal;

import colesico.framework.http.HttpContext;
import colesico.framework.http.HttpRequest;
import colesico.framework.http.HttpResponse;
import colesico.framework.httpserver.ErrorHandler;
import colesico.framework.httpserver.RequestProcessor;
import colesico.framework.ioc.message.Message;
import colesico.framework.ioc.scope.RequestScope;
import colesico.framework.ioc.scope.ThreadScope;
import colesico.framework.ioc.scope.Unscoped;
import colesico.framework.router.Router.Invocation;
import colesico.framework.router.Router;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

import jakarta.inject.Inject;

/**
 * This processor is used to perform blocking requests processing and
 * dispatched from UndertowRequestProcessor
 */
@Unscoped
public class UndertowBlockingHandler extends RequestProcessor<HttpServerExchange> implements HttpHandler {

    private final Router.Invocation resolution;

    @Inject
    public UndertowBlockingHandler(@Message Invocation resolution, RequestScope requestScope, Router router, ErrorHandler errorHandler) {
        super(requestScope, router, errorHandler);
        this.resolution = resolution;
    }


    @Override
    protected HttpContext createHttpContext(HttpServerExchange rawContext) {
        return new HttpContext(new UndertowHttpRequest(rawContext), new UndertowHttpResponse(rawContext));
    }

    @Override
    protected Router.Invocation resolve(HttpContext httpContext) {
        return resolution;
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) {
        try {
            exchange.startBlocking();
            super.handleRequest(exchange);
        } finally {
            exchange.endExchange();
        }
    }
}
