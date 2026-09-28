package colesico.framework.restlet.jsonb;

import io.avaje.json.JsonAdapter;
import io.avaje.json.JsonReader;
import io.avaje.json.JsonWriter;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

public final class IsoLocalDateTimeAdapter implements JsonAdapter<LocalDateTime> {

    private static final DateTimeFormatter PARSER = new DateTimeFormatterBuilder()
            .append(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            .optionalStart()
            .appendOffsetId()
            .toFormatter();

    private static final DateTimeFormatter PRINTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    public void toJson(JsonWriter writer, LocalDateTime value) {
        if (value == null) {
            writer.nullValue();
        } else {
            writer.value(value.format(PRINTER));
        }
    }

    @Override
    public LocalDateTime fromJson(JsonReader reader) {
        if (reader.isNullValue()) {
            return null;
        }

        String val = reader.readString();
        var temporal = PARSER.parseBest(val, OffsetDateTime::from, LocalDateTime::from);

        if (temporal instanceof OffsetDateTime offsetDateTime) {
            return offsetDateTime.toLocalDateTime();
        }
        return (LocalDateTime) temporal;
    }
}