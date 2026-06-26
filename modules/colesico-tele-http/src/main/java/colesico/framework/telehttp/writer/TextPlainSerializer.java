package colesico.framework.telehttp.writer;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * {@link String#valueOf(Object)} based serializer
 */
public class TextPlainSerializer implements ValueSerializer {

    public static final String MIME_TYPE = "text/plain";

    protected Charset charset(String charsetName) {
        if (charsetName != null) {
            return Charset.forName(charsetName);
        }
        return StandardCharsets.UTF_8;
    }

    @Override
    public void serialize(Object value, Map<String, String> mediaParams, OutputStream outputStream) {
        if (value == null) {
            return;
        }
        try {
            outputStream.write(String.valueOf(value).getBytes(charset(mediaParams.get("charset"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
