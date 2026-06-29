package colesico.framework.telehttp.writer;

import colesico.framework.telehttp.MediaType;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * {@link String#valueOf(Object)} based serializer
 */
public class ToStringSerializer implements ValueSerializer {

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

        String stringValue;
        if (value instanceof String str) {
            stringValue = str;
        } else {
            stringValue = value.toString();
        }

        try {
            outputStream.write(stringValue.getBytes(charset(mediaParams.get(MediaType.CHARSET_PARAM))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
