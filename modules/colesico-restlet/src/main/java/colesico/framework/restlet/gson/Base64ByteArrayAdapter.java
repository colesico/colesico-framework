package colesico.framework.restlet.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.Base64;

public class Base64ByteArrayAdapter extends TypeAdapter<byte[]> {

    @Override
    public byte[] read(JsonReader reader) throws IOException {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return null;
        }
        String base64String = reader.nextString();
        return Base64.getDecoder().decode(base64String);
    }

    @Override
    public void write(JsonWriter writer, byte[] value) throws IOException {
        if (value == null) {
            writer.nullValue();
            return;
        }
        String base64String = Base64.getEncoder().encodeToString(value);
        writer.value(base64String);
    }

}