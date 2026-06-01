package colesico.framework.fusionhttp.internal;

import colesico.framework.http.HttpContext;
import colesico.framework.httpserver.ErrorHandler;
import colesico.framework.httpserver.RequestProcessor;
import colesico.framework.ioc.scope.RequestScope;
import colesico.framework.router.Router;
import io.fusionauth.http.server.HTTPHandler;
import io.fusionauth.http.server.HTTPRequest;
import io.fusionauth.http.server.HTTPResponse;

public class FusionHttpHandler extends RequestProcessor<FusionHttpContext> implements HTTPHandler {

    public FusionHttpHandler(RequestScope requestScope, Router router, ErrorHandler errorHandler) {
        super(requestScope, router, errorHandler);
    }

    @Override
    public void handle(HTTPRequest httpRequest, HTTPResponse httpResponse) throws Exception {
        var context =  new FusionHttpContext(httpRequest,httpResponse);
        handleRequest(context);
    }

    @Override
    protected HttpContext createHttpContext(FusionHttpContext ctx) {
        return new HttpContext(ctx.);

    }
}
