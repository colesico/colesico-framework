package colesico.framework.restlet.teleapi.converter;

import com.jsoniter.JsonIterator;
import com.jsoniter.output.JsonStream;

import java.io.InputStream;

public class JsonIterConverter implements JsonConverter {

    public static final int BUFFER_SIZE = 1024 * 10;

    @Override
    public String toJson(Object obj) {
        return JsonStream.serialize(obj);
    }

    @Override
    public <T> T fromJson(String json, Class<T> type) {
        return JsonIterator.deserialize(json, type);
    }

    @Override
    public <T> T fromJson(InputStream is, Class<T> type) {
        try {
            return JsonIterator.parse(is, BUFFER_SIZE).read(type);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
