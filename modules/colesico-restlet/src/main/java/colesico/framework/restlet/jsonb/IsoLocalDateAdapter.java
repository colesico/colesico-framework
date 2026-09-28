package colesico.framework.restlet.jsonb;

import io.avaje.jsonb.CustomAdapter;
import io.avaje.json.JsonAdapter;
import io.avaje.json.JsonReader;
import io.avaje.json.JsonWriter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public final class IsoLocalDateAdapter implements JsonAdapter<LocalDate> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    @Override
    public void toJson(JsonWriter writer, LocalDate value) {
        if (value == null) {
            writer.nullValue();
        } else {
            writer.value(value.format(FORMATTER));
        }
    }

    @Override
    public LocalDate fromJson(JsonReader reader) {
        if (reader.isNullValue()) {
            return null;
        }
        return LocalDate.parse(reader.readString(), FORMATTER);
    }
}