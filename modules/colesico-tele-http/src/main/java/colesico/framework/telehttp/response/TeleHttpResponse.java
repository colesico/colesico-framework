package colesico.framework.telehttp.response;

import colesico.framework.http.HttpCookie;

import java.util.*;

/**
 * General value model
 */
abstract public class TeleHttpResponse {

    /**
     * Http value status code
     */
    protected final Integer statusCode;

    /**
     * Http value content type
     */
    protected final String contentType;

    protected final Map<String, List<String>> headers = new HashMap<>();

    protected final Set<HttpCookie> cookies = new HashSet<>();

    public TeleHttpResponse(Integer statusCode, String contentType) {
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

    public String contentType() {
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
