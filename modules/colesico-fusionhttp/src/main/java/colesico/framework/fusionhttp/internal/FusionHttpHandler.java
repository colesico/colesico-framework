package colesico.framework.fusionhttp.internal;

import colesico.framework.http.HttpContext;
import colesico.framework.httpserver.ErrorHandler;
import colesico.framework.httpserver.RequestProcessor;
import colesico.framework.ioc.scope.TaskScope;
import colesico.framework.httprouter.Router;
import io.fusionauth.http.server.HTTPHandler;
import io.fusionauth.http.server.HTTPRequest;
import io.fusionauth.http.server.HTTPResponse;

public class FusionHttpHandler extends RequestProcessor<FusionHttpContext> implements HTTPHandler {

    public FusionHttpHandler(TaskScope taskScope, Router router, ErrorHandler errorHandler) {
        super(taskScope, router, errorHandler);
    }

    @Override
    public void handle(HTTPRequest request, HTTPResponse response) throws Exception {
        var context = new FusionHttpContext(new FusionHttpRequest(request),
                new FusionHttpResponse(response)
        );
        handleRequest(context);
    }

    @Override
    protected HttpContext createHttpContext(FusionHttpContext ctx) {
        return new HttpContext(ctx.request(), ctx.response());
    }
}
