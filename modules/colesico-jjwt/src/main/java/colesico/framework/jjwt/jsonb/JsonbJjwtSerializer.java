package colesico.framework.jjwt.jsonb;

import io.avaje.jsonb.Jsonb;
import io.jsonwebtoken.io.SerializationException;
import io.jsonwebtoken.io.Serializer;

import java.io.OutputStream;
import java.util.Map;

public final class JsonbJjwtSerializer implements Serializer<Map<String, ?>> {

    private final Jsonb jsonb;

    public JsonbJjwtSerializer(Jsonb jsonb) {
        this.jsonb = jsonb;
    }

    @Override
    public byte[] serialize(Map<String, ?> stringMap) throws SerializationException {
        return jsonb.toJsonBytes(stringMap);
    }

    @Override
    public void serialize(Map<String, ?> stringMap, OutputStream outputStream) throws SerializationException {
        try {
            jsonb.toJson(stringMap, outputStream);
        } catch (Exception e) {
            throw new SerializationException("Failed to serialize map to JSON", e);
        }
    }
}