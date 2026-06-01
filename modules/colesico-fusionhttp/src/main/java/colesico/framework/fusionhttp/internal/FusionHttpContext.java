package colesico.framework.fusionhttp.internal;

import io.fusionauth.http.server.HTTPRequest;
import io.fusionauth.http.server.HTTPResponse;

public record FusionHttpContext(
    HTTPRequest request,
    HTTPResponse response
){}
