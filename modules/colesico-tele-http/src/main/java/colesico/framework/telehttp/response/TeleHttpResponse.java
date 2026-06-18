package colesico.framework.telehttp.response;

import colesico.framework.http.HttpCookie;

import java.util.*;

/**
 * Basic response model
 */
abstract public class TeleHttpResponse {

    public static final Integer DEFAULT_STATUS_CODE = 200;

    /**
     * Http response content type
     */
    protected final String contentType;

    /**
     * Http response status code
     */
    protected final Integer statusCode;

    protected final Map<String, List<String>> headers = new HashMap<>();

    protected final Set<HttpCookie> cookies = new HashSet<>();

    public TeleHttpResponse(String contentType, Integer statusCode) {
        this.contentType = contentType;
        this.statusCode = statusCode;
    }

    public void addHeader(String name, String vale) {
        List<String> hValues = headers.computeIfAbsent(name, n -> new ArrayList<>());
        hValues.add(vale);
    }

    public void addCookie(HttpCookie cookie) {
        cookies.add(cookie);
    }

    public String contentType() {
        return contentType;
    }

    public Integer statusCode() {
        return statusCode;
    }

    public Map<String, List<String>> headers() {
        return headers;
    }

    public Set<HttpCookie> cookies() {
        return cookies;
    }
}
