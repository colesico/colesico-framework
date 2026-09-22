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

    ContentType contentType();

    Map<String, List<String>> headers();

    Set<HttpCookie> cookies();
}
