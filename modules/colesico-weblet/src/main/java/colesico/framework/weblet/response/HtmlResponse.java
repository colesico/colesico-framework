package colesico.framework.weblet.response;

import colesico.framework.telehttp.ContentType;
import colesico.framework.telehttp.response.StringResponse;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Simple HTML response
 */
public class HtmlResponse extends StringResponse {
    public HtmlResponse(Integer statusCode, Charset charset, String content) {
        super(statusCode, ContentType.of("text/html", charset), content);
    }

    public static HtmlResponse of(String content) {
        return new HtmlResponse(null, StandardCharsets.UTF_8, content);
    }
}
