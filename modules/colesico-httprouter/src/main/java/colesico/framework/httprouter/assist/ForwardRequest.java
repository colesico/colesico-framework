package colesico.framework.httprouter.assist;

import colesico.framework.http.*;
import colesico.framework.httprouter.RouterException;

import java.io.InputStream;
import java.io.Writer;
import java.net.URI;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Http request proxy to perform passing query parameters in forward operation
 */
public class ForwardRequest implements HttpRequest {

    private final HttpRequest parentRequest;

    private String requestURI;
    private String queryString;
    private HttpValues<String, String> queryParameters;

    public ForwardRequest(HttpRequest parentRequest,
                          String forwardURI) {
        this.parentRequest = parentRequest;
        parseForwardURI(forwardURI);
    }

    protected void parseForwardURI(String forwardURI) {
        try {
            URI uri = new URI(forwardURI);
            requestURI = uri.getPath();
            queryString = uri.getQuery();

            Map<String, List<String>> paramsMap = new HashMap<>();
            if (queryString != null) {
                // Parse params
                String[] pairs = queryString.split("&");
                for (String pair : pairs) {
                    int idx = pair.indexOf("=");
                    String paramName = URLDecoder.decode(pair.substring(0, idx), "UTF-8");
                    String paramVal = URLDecoder.decode(pair.substring(idx + 1), "UTF-8");
                    paramsMap.put(paramName, new ArrayList<>(List.of(paramVal)));
                }
            }
            queryParameters = new HttpValues<>(paramsMap);
        } catch (Exception e) {
            throw new RouterException(e);
        }
    }

    @Override
    public HttpMethod method() {
        return parentRequest.method();
    }

    @Override
    public String scheme() {
        return parentRequest.scheme();
    }

    @Override
    public String host() {
        return parentRequest.host();
    }

    @Override
    public Integer port() {
        return parentRequest.port();
    }

    @Override
    public String path() {
        return requestURI;
    }

    @Override
    public String queryString() {
        return queryString;
    }

    @Override
    public HttpValues<String, String> headers() {
        return parentRequest.headers();
    }

    @Override
    public HttpValues<String, HttpCookie> cookies() {
        return parentRequest.cookies();
    }

    @Override
    public HttpValues<String, String> queryParameters() {
        return queryParameters;
    }

    @Override
    public HttpValues<String, String> formData() {
        return parentRequest.formData();
    }

    @Override
    public HttpValues<String, HttpFile> files() {
        return parentRequest.files();
    }

    @Override
    public InputStream inputStream() {
        return parentRequest.inputStream();
    }

    @Override
    public void dump(Writer out) {
        parentRequest.dump(out);
    }

    public HttpRequest parentRequest() {
        return parentRequest;
    }
}
