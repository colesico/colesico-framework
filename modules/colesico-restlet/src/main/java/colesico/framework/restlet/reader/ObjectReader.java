package colesico.framework.restlet.reader;

import colesico.framework.assist.StringUtils;
import colesico.framework.http.HttpContext;
import colesico.framework.http.HttpMethod;
import colesico.framework.restlet.*;
import colesico.framework.telehttp.origin.Origin;
import colesico.framework.telehttp.origin.OriginFactory;

import colesico.framework.telehttp.reader.OriginReader;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;

import java.io.InputStream;

import static colesico.framework.http.HttpMethod.*;

@Singleton
public final class ObjectReader
        extends OriginReader<Object, RestletReadOptions>
        implements RestletReader<Object> {

    private final JsonSerializer serializer;

    private final Provider<HttpContext> httpContext;

    @Inject
    public ObjectReader(OriginFactory originFactory, JsonSerializer serializer, Provider<HttpContext> httpContext) {
        super(originFactory);
        this.serializer = serializer;
        this.httpContext = httpContext;
    }

    @Override
    public Object read(RestletReadOptions options) {
        var httpRequest = httpContext.get().request();
        try {
            if (readInputStream(options.originName(), httpRequest.method())) {
                try (InputStream is = httpRequest.inputStream()) {
                    return serializer.deserialize(options.baseType(), null, is);
                }
            }

            String strValue = readString(options.originName(), options.paramName());
            return StringUtils.isBlank(strValue) ? null : serializer.deserialize(strValue, options.baseType());
        } catch (Exception e) {
            throw RestletException.of(e, 400);
        }
    }

    private boolean readInputStream(String originName, HttpMethod method) {
        return switch (originName) {
            case Origin.BODY -> true;
            case Origin.AUTO -> method.is(POST) || method.is(PATCH) || method.is(DELETE) || method.is(PUT);
            default -> false;
        };
    }
}
