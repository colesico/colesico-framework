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

    protected final Map<String, List<String>> headers;

    protected final Set<HttpCookie> cookies;

    public TeleHttpResponse(Integer statusCode, ContentType contentType, Map<String, List<String>> headers, Set<HttpCookie> cookies) {
        this.statusCode = statusCode;
        this.contentType = contentType;
        this.headers = headers;
        this.cookies = cookies;
    }

    public TeleHttpResponse addHeader(String name, String value) {
        List<String> hValues = headers.computeIfAbsent(name, n -> new ArrayList<>());
        hValues.add(value);
        return this;
    }

    public TeleHttpResponse addCookie(HttpCookie cookie) {
        cookies.add(cookie);
        return this;
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

    abstract public static class Builder<R extends TeleHttpResponse, B extends Builder<R, B>> {
        protected Integer statusCode;
        protected ContentType contentType;
        protected final Map<String, List<String>> headers = new HashMap<>();
        protected final Set<HttpCookie> cookies = new HashSet<>();

        abstract public R build();

        abstract protected B self();

        public B statusCode(Integer statusCode) {
            this.statusCode = statusCode;
            return self();
        }

        public B contentType(ContentType contentType) {
            this.contentType = contentType;
            return self();
        }

        public B header(String name, String value) {
            List<String> hValues = headers.computeIfAbsent(name, n -> new ArrayList<>());
            hValues.add(value);
            return self();
        }

        public B cookie(HttpCookie cookie) {
            this.cookies.add(cookie);
            return self();
        }
    }
}
