package colesico.framework.restlet.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class IsoLocalDateAdapter extends TypeAdapter<LocalDate> {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    @Override
    public LocalDate read(JsonReader reader) throws IOException {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return null;
        }
        return LocalDate.parse(reader.nextString(), FORMATTER);
    }

    @Override
    public void write(JsonWriter writer, LocalDate value) throws IOException {
        if (value == null) {
            writer.nullValue();
        } else {
            writer.value(value.format(FORMATTER));
        }
    }
}