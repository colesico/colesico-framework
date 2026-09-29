package colesico.framework.restlet.moshi;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class IsoDateAdapter extends JsonAdapter<Date> {
    private static final String PATTERN = "yyyy-MM-dd'T'HH:mm:ss";

    @Override
    public Date fromJson(JsonReader reader) throws IOException {
        String dateString = reader.nextString();
        try {
            return new SimpleDateFormat(PATTERN).parse(dateString);
        } catch (Exception e) {
            throw new IOException("Date parsing error[" + dateString + "]: " + e.getMessage(), e);
        }
    }

    @Override
    public void toJson(JsonWriter writer, Date value) throws IOException {
        if (value == null) {
            writer.nullValue();
            return;
        }
        String dateString = new SimpleDateFormat(PATTERN).format(value);
        writer.value(dateString);
    }
}
