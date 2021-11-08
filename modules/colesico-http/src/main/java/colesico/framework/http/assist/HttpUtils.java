package colesico.framework.http.assist;

import colesico.framework.http.HttpCookie;
import colesico.framework.http.HttpResponse;

import java.util.List;
import java.util.Map;
import java.util.Set;

public final class HttpUtils {

    public static void setCookies(HttpResponse response, Set<HttpCookie> cookies) {
        for (HttpCookie cookie : cookies) {
            response.setCookie(cookie);
        }
    }

    public static void setHeaders(HttpResponse response, Map<String, List<String>> headers) {
        for (Map.Entry<String, List<String>> h : headers.entrySet()) {
            for (String v : h.getValue()) {
                response.setHeader(h.getKey(), v);
            }
        }
    }
}
