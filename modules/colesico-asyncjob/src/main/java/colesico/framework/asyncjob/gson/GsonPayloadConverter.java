package colesico.framework.asyncjob.gson;

import colesico.framework.ioc.Polysupplier;
import colesico.framework.asyncjob.PayloadConverter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

@Singleton
public class GsonPayloadConverter implements PayloadConverter {

    protected final Gson gson;

    public GsonPayloadConverter(Polysupplier<JobGsonOptionsPrototype> options) {
        final GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithModifiers(Modifier.STATIC, Modifier.TRANSIENT, Modifier.VOLATILE);
        options.forEach(o -> o.applyOptions(builder), null);
        this.gson = builder.create();
    }
    @Override
    public Object toObject(Type payloadType, String payloadStr) {
        return gson.fromJson(payloadStr,payloadType);
    }

    @Override
    public String fromObject(Object payload) {
        return gson.toJson(payload);
    }
}
