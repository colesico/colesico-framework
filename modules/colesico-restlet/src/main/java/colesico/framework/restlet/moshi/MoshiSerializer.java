package colesico.framework.restlet.moshi;

import colesico.framework.ioc.production.Polysupplier;
import colesico.framework.restlet.JsonSerializer;
import com.squareup.moshi.Moshi;
import jakarta.inject.Singleton;

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

    }

    @Override
    public <T> T deserialize(InputStream inputStream, Charset charset, Type targetType) {
        return null;
    }
}
