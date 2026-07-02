package colesico.framework.telehttp.response;

import colesico.framework.http.HttpCookie;
import colesico.framework.telehttp.ContentType;

import java.util.*;

/**
 * General tele-http response model
 */
abstract public class TeleHttpResponse {

    /**
     * Http status code
     */
    protected final Integer statusCode;

    /**
     * Content-type
     */
    protected final ContentType contentType;

    protected final Map<String, List<String>> headers = new HashMap<>();

    protected final Set<HttpCookie> cookies = new HashSet<>();

    public TeleHttpResponse(Integer statusCode, ContentType contentType) {
        this.statusCode = statusCode;
        this.contentType = contentType;
    }

    public void addHeader(String name, String vale) {
        List<String> hValues = headers.computeIfAbsent(name, n -> new ArrayList<>());
        hValues.add(vale);
    }

    public void addCookie(HttpCookie cookie) {
        cookies.add(cookie);
    }

    public Integer statusCode() {
        return statusCode;
    }

    public ContentType contentType() {
        return contentType;
    }

    public Map<String, List<String>> headers() {
        return headers;
    }

    public Set<HttpCookie> cookies() {
        return cookies;
    }

    public final DynamicResponse toDynamic() {
        return DynamicResponse.of(this);
    }
}
