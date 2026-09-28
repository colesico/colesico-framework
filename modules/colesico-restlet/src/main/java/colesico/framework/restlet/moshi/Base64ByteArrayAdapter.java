package colesico.framework.restlet.moshi;

import com.squareup.moshi.*;

import java.io.IOException;
import java.util.Base64;

public class Base64ByteArrayAdapter extends JsonAdapter<byte[]> {

    @Override
    public byte[] fromJson(JsonReader reader) throws IOException {
        String base64String = reader.nextString();
        return Base64.getDecoder().decode(base64String);
    }

    @Override
    public void toJson(JsonWriter writer, byte[] value) throws IOException {
        if (value == null) {
            writer.nullValue();
            return;
        }
        String base64String = Base64.getEncoder().encodeToString(value);
        writer.value(base64String);
    }
}