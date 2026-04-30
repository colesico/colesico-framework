package colesico.framework.telehttp;

import colesico.framework.http.HttpCookie;
import colesico.framework.teleapi.dataport.TWContext;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Basic tele-writing context for interaction via http
 */
abstract public class HttpTWContext<T extends Type, P> extends TWContext<T, P> {

    protected final String contentType;

    /**
     * HTTP status code
     */
    protected final Integer statusCode;

    /**
     * HTTP headers
     */
    protected final Map<String, String> headers;

    /**
     * Cookies
     */
    protected final Iterable<HttpCookie> cookies;

    public HttpTWContext(T valueType,
                         String contentType,
                         Integer statusCode,
                         Map<String, String> headers,
                         Iterable<HttpCookie> cookies,
                         P payload) {

        super(valueType, payload);
        this.contentType = contentType;
        this.statusCode = statusCode;
        this.headers = headers;
        this.cookies = cookies;
    }

    public String contentType() {
        return contentType;
    }

    public Integer statusCode() {
        return statusCode;
    }

    public Map<String, String> headers() {
        return headers;
    }

    public Iterable<HttpCookie> cookies() {
        return cookies;
    }

}
