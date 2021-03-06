package colesico.framework.router.assist;

import colesico.framework.http.*;

import java.io.InputStream;
import java.io.Writer;
import java.net.URI;
import java.net.URLDecoder;
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

            Map<String, MultiValue<String>> paramsMap = new HashMap<>();
            if (queryString != null) {
                // Parse params
                String[] pairs = queryString.split("&");
                for (String pair : pairs) {
                    int idx = pair.indexOf("=");
                    String paramName = URLDecoder.decode(pair.substring(0, idx), "UTF-8");
                    String paramVal = URLDecoder.decode(pair.substring(idx + 1), "UTF-8");
                    paramsMap.put(paramName, new MultiValue<>(List.of(paramVal)));
                }
            }
            queryParameters = new HttpValues<>(paramsMap);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public HttpMethod getRequestMethod() {
        return parentRequest.getRequestMethod();
    }

    @Override
    public String getRequestScheme() {
        return parentRequest.getRequestScheme();
    }

    @Override
    public String getHost() {
        return parentRequest.getHost();
    }

    @Override
    public Integer getPort() {
        return parentRequest.getPort();
    }

    @Override
    public String getRequestURI() {
        return requestURI;
    }

    @Override
    public String getQueryString() {
        return queryString;
    }

    @Override
    public HttpValues<String, String> getHeaders() {
        return parentRequest.getHeaders();
    }

    @Override
    public HttpValues<String, HttpCookie> getCookies() {
        return parentRequest.getCookies();
    }

    @Override
    public HttpValues<String, String> getQueryParameters() {
        return queryParameters;
    }

    @Override
    public HttpValues<String, String> getPostParameters() {
        return parentRequest.getPostParameters();
    }

    @Override
    public HttpValues<String, HttpFile> getPostFiles() {
        return parentRequest.getPostFiles();
    }

    @Override
    public InputStream getInputStream() {
        return parentRequest.getInputStream();
    }

    @Override
    public void dump(Writer out) {
        parentRequest.dump(out);
    }

    public HttpRequest getParentRequest() {
        return parentRequest;
    }
}
