package colesico.framework.restlet.teleapi.gson;

import colesico.framework.ioc.Polysupplier;
import colesico.framework.restlet.teleapi.RestletJsonConverter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;
import java.io.*;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.time.LocalDateTime;

/**
 * Default json converter for restlet
 */
@Singleton
public class GsonConverter implements RestletJsonConverter {

    protected final Gson gson;

    public GsonConverter(Polysupplier<RestletGsonOptions> options) {
        final GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithModifiers(Modifier.STATIC, Modifier.TRANSIENT, Modifier.VOLATILE)
                .registerTypeHierarchyAdapter(byte[].class, new GsonByteArrayToBase64())
                .registerTypeAdapter(LocalDateTime.class, new GsonLocalDateTime())
        ;
        options.forEach(o -> o.applyOptions(builder), null);
        this.gson = builder.create();
    }

    @Override
    public <T> String toJson(T obj) {
        return gson.toJson(obj);
    }

    @Override
    public <T> T fromJson(Reader reader, Type valueType) {
        return gson.fromJson(reader, valueType);
    }
}
