package colesico.framework.restlet.moshi;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

class IsoLocalDateAdapter extends JsonAdapter<LocalDate> {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    @Override
    public LocalDate fromJson(JsonReader reader) throws IOException {
        return LocalDate.parse(reader.nextString(), FORMATTER);
    }

    @Override
    public void toJson(JsonWriter writer, LocalDate value) throws IOException {
        if (value == null) {
            writer.nullValue();
        } else {
            writer.value(value.format(FORMATTER));
        }
    }
}