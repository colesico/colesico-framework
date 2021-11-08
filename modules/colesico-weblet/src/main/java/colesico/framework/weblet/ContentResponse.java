package colesico.framework.weblet;

import colesico.framework.http.HttpCookie;

import java.util.*;

abstract public class ContentResponse {

    public static final int DEFAULT_STATUS_CODE = 200;

    /**
     * Http response content type
     */
    protected final String contentType;

    /**
     * Http response status code
     */
    protected final int statusCode;

    private Map<String, List<String>> headers = new HashMap<>();

    private Set<HttpCookie> cookies = new HashSet<>();

    public ContentResponse(String contentType, int statusCode) {
        this.contentType = contentType;
        this.statusCode = statusCode;
    }

    public void setHeader(String name, String vale) {
        List<String> hValues = headers.computeIfAbsent(name, n -> new ArrayList<>());
        hValues.add(vale);
    }

    public void setCookie(HttpCookie cookie) {
        cookies.add(cookie);
    }

    public String getContentType() {
        return contentType;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    public Set<HttpCookie> getCookies() {
        return cookies;
    }
}
