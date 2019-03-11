package colesico.framework.restlet.teleapi.converter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.lang.reflect.Modifier;

public class GsonConverter implements JsonConverter {

    private final Gson gson;

    public GsonConverter() {
        this.gson = new GsonBuilder()
                .excludeFieldsWithModifiers(Modifier.STATIC, Modifier.TRANSIENT, Modifier.VOLATILE)
                .registerTypeHierarchyAdapter(byte[].class, new GsonByteArrayToBase64())
                .create();
    }

    @Override
    public <T> String toJson(T obj) {
        return gson.toJson(obj);
    }

    @Override
    public <T> T fromJson(String json, Class<T> type) {
        return gson.fromJson(json, type);
    }

    @Override
    public <T> T fromJson(InputStream is, Class<T> type) {
        try (Reader reader = new InputStreamReader(is, "UTF-8")){
            T result = gson.fromJson(reader, type);
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
