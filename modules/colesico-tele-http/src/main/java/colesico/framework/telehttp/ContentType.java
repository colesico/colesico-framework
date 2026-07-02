package colesico.framework.telehttp;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Content-type metadata
 *
 * @param mimeType
 * @param parameters
 */
public record ContentType(String mimeType, Map<String, String> parameters) {

    public static final String CHARSET_PARAM = "charset";

    public static final ContentType TEXT_PLAIN = ContentType.of("text/plain", StandardCharsets.UTF_8);
    public static final ContentType TEXT_HTML = ContentType.of("text/html", StandardCharsets.UTF_8);
    public static final ContentType APPLICATION_OCTET_STREAM = ContentType.of("application/octet-stream");
    public static final ContentType APPLICATION_JSON = ContentType.of("application/json", StandardCharsets.UTF_8);
    public static final ContentType APPLICATION_XML = ContentType.of("application/xml", StandardCharsets.UTF_8);
    public static final ContentType MULTIPART_FORM_DATA = ContentType.of("multipart/form-data", StandardCharsets.UTF_8);

    public static ContentType of(String mimeType) {
        return new ContentType(mimeType, Map.of());
    }

    public static ContentType of(String mimeType, Map<String, String> parameters) {
        return new ContentType(mimeType, parameters);
    }

    public static ContentType of(String mimeType, String... parameters) {
        Map<String, String> paramsMap = new HashMap<>();
        for (int i = 0; i < parameters.length; i += 2) {
            paramsMap.put(parameters[i], parameters[i + 1]);
        }
        return new ContentType(mimeType, paramsMap);
    }

    public static ContentType of(String mimeType, Charset charset) {
        return new ContentType(mimeType, Map.of(CHARSET_PARAM, charset.name()));
    }

    public Optional<Charset> charset() {
        if (parameters == null) {
            return Optional.empty();
        }
        var charsetName = parameters.get(CHARSET_PARAM);
        if (charsetName != null) {
            return Optional.of(Charset.forName(charsetName));
        }
        return Optional.empty();
    }

    public String headerValue() {
        StringBuilder result = new StringBuilder(mimeType);
        parameters.forEach((name, value) -> {
            result.append("; ").append(name).append("=").append(value);
        });
        return result.toString();
    }

}
