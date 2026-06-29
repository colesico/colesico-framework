package colesico.framework.telehttp;

import java.nio.charset.StandardCharsets;
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
    public static final String UTF8_CHARSET = "utf-8";

    public static final MediaType TEXT_PLAIN = MediaType.ofCharset("text/plain", UTF8_CHARSET);
    public static final MediaType TEXT_HTML = MediaType.ofCharset("text/html", UTF8_CHARSET);
    public static final MediaType APPLICATION_OCTET_STREAM = MediaType.ofCharset("application/octet-stream", UTF8_CHARSET);
    public static final MediaType APPLICATION_JSON = MediaType.ofCharset("application/json", UTF8_CHARSET);

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
