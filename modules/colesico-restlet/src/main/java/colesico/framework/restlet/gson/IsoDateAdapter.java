package colesico.framework.restlet.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class IsoDateAdapter extends TypeAdapter<Date> {
    private static final String PATTERN = "yyyy-MM-dd'T'HH:mm:ss";

    @Override
    public Date read(JsonReader reader) throws IOException {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return null;
        }
        String dateString = reader.nextString();
        try {
            return new SimpleDateFormat(PATTERN).parse(dateString);
        } catch (Exception e) {
            throw new IOException("Date parsing error[" + dateString + "]: " + e.getMessage(), e);
        }
    }

    @Override
    public void write(JsonWriter writer, Date value) throws IOException {
        if (value == null) {
            writer.nullValue();
            return;
        }
        String dateString = new SimpleDateFormat(PATTERN).format(value);
        writer.value(dateString);
    }
}
