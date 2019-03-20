package colesico.framework.restlet.teleapi.converter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

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
    public <T> T fromJson(Reader reader, Type valueType) {
        return gson.fromJson(reader, valueType);
    }
}
