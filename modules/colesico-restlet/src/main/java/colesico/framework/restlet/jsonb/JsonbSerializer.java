package colesico.framework.restlet.jsonb;

import colesico.framework.ioc.production.Polysupplier;
import colesico.framework.restlet.JsonSerializer;
import colesico.framework.restlet.RestletException;
import io.avaje.jsonb.Jsonb;
import jakarta.inject.Singleton;

import java.io.*;

import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Singleton
public class JsonbSerializer implements JsonSerializer {

    private final Jsonb jsonb;

    public JsonbSerializer(Polysupplier<JsonbOptionsPrototype> options) {
        final Jsonb.Builder builder = Jsonb.builder();

        builder.add(LocalDate.class, new IsoLocalDateAdapter())
                .add(LocalDateTime.class, new IsoLocalDateTimeAdapter())
                .add(Date.class, new IsoDateAdapter());


        options.forEach(o -> o.configure(builder));
        this.jsonb = builder.build();
    }

    @Override
    public void serialize(Object value, Type baseType, Charset charset, OutputStream outputStream) {
        try (Writer writer = new OutputStreamWriter(outputStream, charset)) {
            jsonb.toJson(value, writer);
        } catch (IOException e) {
            throw new RestletException(e, 500);
        }
    }

    @Override
    public <T> T deserialize(InputStream inputStream, Charset charset, Type targetType) {
        try (Reader reader = new InputStreamReader(inputStream, charset)) {
            return (T) jsonb.type(targetType).fromJson(reader);
        } catch (IOException e) {
            throw new RestletException(e, 500);
        }
    }
}
