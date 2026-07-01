package colesico.framework.telehttp;

import java.util.HashMap;
import java.util.Map;

/**
 * Content-type data
 *
 * @param mimeType
 * @param parameters
 */
public record MediaType(String mimeType, Map<String, String> parameters) {

    public static final String CHARSET_PARAM = "charset";
    public static final String CHARSET_UTF8 = "utf-8";

    public static final MediaType TEXT_PLAIN = MediaType.ofCharset("text/plain", CHARSET_UTF8);
    public static final MediaType TEXT_HTML = MediaType.ofCharset("text/html", CHARSET_UTF8);
    public static final MediaType APPLICATION_OCTET_STREAM = MediaType.of("application/octet-stream");
    public static final MediaType APPLICATION_JSON = MediaType.ofCharset("application/json", CHARSET_UTF8);
    public static final MediaType APPLICATION_XML = MediaType.ofCharset("application/xml", CHARSET_UTF8);
    public static final MediaType  MULTIPART_FORM_DATA  = MediaType.ofCharset("multipart/form-data", CHARSET_UTF8);

    public static MediaType of(String mimeType) {
        return new MediaType(mimeType, Map.of());
    }

    public static MediaType ofParams(String mimeType, String... parameters) {
        Map<String, String> paramsMap = new HashMap<>();

        for (int i = 0; i < parameters.length; i += 2) {
            paramsMap.put(parameters[i], parameters[i + 1]);
        }
        return new MediaType(mimeType, paramsMap);
    }

    public static MediaType ofCharset(String mimeType, String charset) {
        return new MediaType(mimeType, Map.of(CHARSET_PARAM, charset));
    }

    public String charset() {
        if (parameters == null) {
            return null;
        }
        return parameters.get(CHARSET_PARAM);
    }
}
