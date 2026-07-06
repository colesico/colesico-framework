package colesico.framework.restlet.reader;

import colesico.framework.assist.StringUtils;
import colesico.framework.http.HttpContext;
import colesico.framework.http.HttpMethod;
import colesico.framework.http.HttpRequest;
import colesico.framework.restlet.*;
import colesico.framework.telehttp.origin.Origin;
import colesico.framework.telehttp.origin.OriginFactory;

import colesico.framework.telehttp.reader.OriginReader;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static colesico.framework.http.HttpMethod.*;

@Singleton
public final class JsonObjectReader
        extends OriginReader<Object, RestletReadOptions>
        implements RestletReader<Object> {

    private static final String CONTENT_TYPE_HEADER = "content-type";
    private static final Pattern CHARSET_PATTERN = Pattern.compile("charset=\\s*\"?([^;\"]+)\"?", Pattern.CASE_INSENSITIVE);

    private final JsonSerializer serializer;

    private final Provider<HttpContext> httpContext;

    @Inject
    public JsonObjectReader(OriginFactory originFactory, JsonSerializer serializer, Provider<HttpContext> httpContext) {
        super(originFactory);
        this.serializer = serializer;
        this.httpContext = httpContext;
    }

    @Override
    public Object read(RestletReadOptions options) {
        var httpRequest = httpContext.get().request();
        try {
            if (useInputStream(options.originName(), httpRequest.method())) {
                try (InputStream is = httpRequest.inputStream()) {
                    return serializer.deserialize(is, getCharset(httpRequest), options.baseType());
                }
            }

            String strValue = readString(options.originName(), options.paramName());
            return StringUtils.isBlank(strValue) ? null : serializer.deserialize(strValue, options.baseType());
        } catch (Exception e) {
            throw RestletException.of(e, 400);
        }
    }

    private Charset getCharset(HttpRequest httpRequest) {
        var contentTypeHeader = httpRequest.headers().get(CONTENT_TYPE_HEADER);
        if (contentTypeHeader == null) {
            return StandardCharsets.UTF_8;
        }

        Matcher matcher = CHARSET_PATTERN.matcher(contentTypeHeader);

        if (matcher.find()) {
            try {
                return Charset.forName(matcher.group(1).trim());
            } catch (UnsupportedCharsetException e) {
                return StandardCharsets.UTF_8;
            }
        }

        return StandardCharsets.UTF_8;
    }

    private boolean useInputStream(String originName, HttpMethod method) {
        return switch (originName) {
            case Origin.BODY -> true;
            case Origin.AUTO -> method.is(POST) || method.is(PATCH) || method.is(DELETE) || method.is(PUT);
            default -> false;
        };
    }
}
