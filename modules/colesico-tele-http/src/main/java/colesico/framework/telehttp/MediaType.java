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

    public static final String TEXT_PLAIN = "text/plain";
    public static final String TEXT_HTML = "text/html";
    public static final String APPLICATION_OCTET_STREAM = "application/octet-stream";
    public static final String APPLICATION_JSON = "application/json";

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
        return new MediaType(mimeType, Map.of("charset", charset));
    }

    public String charset() {
        if (parameters == null) {
            return null;
        }
        return parameters.get("charset");
    }
}
