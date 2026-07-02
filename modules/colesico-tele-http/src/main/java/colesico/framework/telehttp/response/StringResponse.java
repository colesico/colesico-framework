package colesico.framework.telehttp.response;

import colesico.framework.telehttp.ContentType;

public class StringResponse extends ValueResponse<String> {
    public StringResponse(Integer statusCode, ContentType contentType, String value) {
        super(statusCode, contentType, value);
    }

    public static StringResponse textPlain(String value) {
        return new StringResponse(null, ContentType.TEXT_PLAIN, value);
    }

    public static StringResponse textHtml(String value) {
        return new StringResponse(null, ContentType.TEXT_HTML, value);
    }

    public static StringResponse of(ContentType contentType, String value) {
        return new StringResponse(null, contentType, value);
    }

    public static StringResponse of(Integer statusCode, ContentType contentType, String value) {
        return new StringResponse(statusCode, contentType, value);
    }
}
