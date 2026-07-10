package colesico.framework.fusionhttp.internal;

public record FusionHttpContext(
    FusionHttpRequest request,
    FusionHttpResponse response
){}
