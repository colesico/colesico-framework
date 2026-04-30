package colesico.framework.telehttp;

import colesico.framework.http.HttpCookie;

import java.util.*;

/**
 * Basic response model
 */
abstract public class HttpTeleResponse<C> {

    public static final int DEFAULT_STATUS_CODE = 200;

    protected final C content;

    /**
     * Http response content type
     */
    protected final String contentType;

    /**
     * Http response status code
     */
    protected final int statusCode;

    protected Map<String, List<String>> headers = new HashMap<>();

    protected Set<HttpCookie> cookies = new HashSet<>();

    protected HttpTeleResponse(C content, String contentType, int statusCode) {
        this.content = content;
        this.contentType = contentType;
        this.statusCode = statusCode;
    }

    public void setHeader(String name, String vale) {
        List<String> hValues = headers.computeIfAbsent(name, n -> new ArrayList<>());
        hValues.add(vale);
    }

    public void addCookie(HttpCookie cookie) {
        cookies.add(cookie);
    }

    public C content() {
        return content;
    }

    public String contentType() {
        return contentType;
    }

    public int statusCode() {
        return statusCode;
    }

    public Map<String, List<String>> headers() {
        return headers;
    }

    public Set<HttpCookie> cookies() {
        return cookies;
    }
}
