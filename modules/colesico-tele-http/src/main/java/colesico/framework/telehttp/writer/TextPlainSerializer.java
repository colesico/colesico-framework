package colesico.framework.telehttp.writer;

import colesico.framework.telehttp.MediaType;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * {@link String#valueOf(Object)} based serializer
 */
public class TextPlainSerializer implements ValueSerializer {

    protected Charset charset(MediaType mediaType) {
        String charsetName = mediaType.charset();
        if (charsetName != null) {
            return Charset.forName(charsetName);
        }
        return StandardCharsets.UTF_8;
    }

    @Override
    public void serialize(Object value, MediaType mediaType, OutputStream outputStream) {
        if (value == null) {
            return;
        }
        try {
            outputStream.write(String.valueOf(value).getBytes(charset(mediaType)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
