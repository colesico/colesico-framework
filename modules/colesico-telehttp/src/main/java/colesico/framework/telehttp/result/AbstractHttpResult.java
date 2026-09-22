package colesico.framework.telehttp.result;

import colesico.framework.http.HttpCookie;
import colesico.framework.telehttp.ContentType;

import java.util.*;

/**
 * Http tele result with basic http entities support
 */
abstract public class AbstractHttpResult implements HttpResult {

    /**
     * Http status
     */
    protected final Integer status;

    protected final ContentType contentType;

    protected final Map<String, List<String>> headers;

    protected final Set<HttpCookie> cookies;

    public AbstractHttpResult(Integer status, ContentType contentType, Map<String, List<String>> headers, Set<HttpCookie> cookies) {
        this.status = status;
        this.contentType = contentType;
        this.headers = headers;
        this.cookies = cookies;
    }

    public AbstractHttpResult addHeader(String name, String value) {
        List<String> hValues = headers.computeIfAbsent(name, n -> new ArrayList<>());
        hValues.add(value);
        return this;
    }

    public AbstractHttpResult addCookie(HttpCookie cookie) {
        cookies.add(cookie);
        return this;
    }

    public Integer status() {
        return status;
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

    abstract public static class Builder<R extends AbstractHttpResult, B extends Builder<R, B>> {
        protected Integer status;
        protected ContentType contentType;
        protected final Map<String, List<String>> headers = new HashMap<>();
        protected final Set<HttpCookie> cookies = new HashSet<>();

        abstract public R build();

        abstract protected B self();

        public B status(Integer status) {
            this.status = status;
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
