package colesico.framework.restlet.teleapi.converter;


import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import okio.BufferedSource;
import okio.Okio;

import javax.inject.Inject;
import java.io.InputStream;

public class MoshiConverter implements JsonConverter {

    private final Moshi moshi;

    @Inject
    public MoshiConverter() {
        moshi = new Moshi.Builder().build();
    }

    @Override
    public <T> String toJson(T obj) {
        try {
            JsonAdapter<T> jsonAdapter = (JsonAdapter<T>) moshi.adapter(obj.getClass());
            return jsonAdapter.toJson(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T fromJson(String json, Class<T> type) {
        try {
            return moshi.adapter(type).fromJson(json);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T fromJson(InputStream is, Class<T> type) {
        try {
            BufferedSource source = Okio.buffer(Okio.source(is));
            return moshi.adapter(type).fromJson(source);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
