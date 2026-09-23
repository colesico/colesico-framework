package colesico.framework.telehttp.result;

import colesico.framework.http.HttpCookie;
import colesico.framework.teleapi.TeleResult;
import colesico.framework.telehttp.ContentType;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Http specific tele-result  (common elements)
 */
public interface HttpResult extends TeleResult {

    Integer status();

    default ContentType contentType() {
        return ContentType.TEXT_PLAIN;
    }

    default Map<String, List<String>> headers() {
        return Map.of();
    }

    default Set<HttpCookie> cookies() {
        return Set.of();
    }
}
