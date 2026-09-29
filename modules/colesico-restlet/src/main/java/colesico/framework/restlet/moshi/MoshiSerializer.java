package colesico.framework.restlet.moshi;

import colesico.framework.ioc.production.Polysupplier;
import colesico.framework.restlet.JsonSerializer;
import colesico.framework.restlet.RestletException;
import com.squareup.moshi.Moshi;
import jakarta.inject.Singleton;
import okio.Okio;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Singleton
public class MoshiSerializer implements JsonSerializer {

    protected Moshi moshi;

    public MoshiSerializer(Polysupplier<MoshiOptionsPrototype> options) {

        Moshi.Builder builder = new Moshi.Builder();

        builder.add(byte[].class, new Base64ByteArrayAdapter())
                .add(LocalDate.class, new IsoLocalDateAdapter())
                .add(LocalDateTime.class, new IsoLocalDateTimeAdapter())
                .add(Date.class, new IsoDateAdapter());

        for (var option : options) {
            builder = option.configure(builder);
        }

        this.moshi = builder.build();
    }

    @Override
    public void serialize(Object value, Type baseType, Charset charset, OutputStream outputStream) {
        try (var sink = Okio.buffer(Okio.sink(outputStream))) {
            moshi.adapter(baseType).toJson(sink, value);
        } catch (java.io.IOException e) {
            throw new RestletException(e, 500);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T deserialize(InputStream inputStream, Charset charset, Type targetType) {
        try (var source = Okio.buffer(Okio.source(inputStream))) {
            return (T) moshi.adapter(targetType).fromJson(source);
        } catch (java.io.IOException e) {
            throw new RestletException(e, 500);
        }
    }
}
