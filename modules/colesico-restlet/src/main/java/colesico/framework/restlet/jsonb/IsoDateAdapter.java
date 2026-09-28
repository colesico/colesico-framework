package colesico.framework.restlet.jsonb;


import io.avaje.json.JsonAdapter;
import io.avaje.json.JsonReader;
import io.avaje.json.JsonWriter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class IsoDateAdapter implements JsonAdapter<Date> {

    private static final String DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";

    @Override
    public void toJson(JsonWriter writer, Date value) {
        if (value == null) {
            writer.nullValue();
        } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);
            writer.value(dateFormat.format(value));
        }
    }

    @Override
    public Date fromJson(JsonReader reader) {
        if (reader.isNullValue()) {
            return null;
        }

        String dateStr = reader.readString();
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);
            return dateFormat.parse(dateStr);
        } catch (ParseException e) {
            throw new IllegalArgumentException(
                    "Invalid ISO date format: [" + dateStr + "]. Expected pattern: yyyy-MM-ddTHH:mm:ss", e
            );
        }
    }
}