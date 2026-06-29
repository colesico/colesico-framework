package colesico.framework.telehttp.response;

import colesico.framework.http.HttpCookie;
import colesico.framework.telehttp.MediaType;

import java.util.*;

/**
 * General tele-http actualResponse model
 */
abstract public class TeleHttpResponse {

    /**
     * Http status code
     */
    protected final Integer statusCode;

    /**
     * Content-type
     */
    protected final MediaType mediaType;

    protected final Map<String, List<String>> headers = new HashMap<>();

    protected final Set<HttpCookie> cookies = new HashSet<>();

    public TeleHttpResponse(Integer statusCode, MediaType mediaType) {
        this.statusCode = statusCode;
        this.mediaType = mediaType;
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

    public MediaType mediaType() {
        return mediaType;
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
