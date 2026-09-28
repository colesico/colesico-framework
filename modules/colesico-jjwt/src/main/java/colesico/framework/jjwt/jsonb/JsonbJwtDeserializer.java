package colesico.framework.jjwt.jsonb;

import io.avaje.jsonb.Jsonb;
import io.jsonwebtoken.io.DeserializationException;
import io.jsonwebtoken.io.Deserializer;

import java.io.Reader;
import java.util.Map;

public final class JsonbJwtDeserializer implements Deserializer<Map<String, ?>> {

    private final Jsonb jsonb = Jsonb.builder().build();

    public JsonbJwtDeserializer() {
    }

    @Override
    public Map<String, ?> deserialize(byte[] bytes) {
        try {
            return jsonb.type(Map.class).fromJson(bytes);
        } catch (Exception e) {
            throw new DeserializationException("Failed to deserialize bytes to Map", e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, ?> deserialize(Reader reader) throws DeserializationException {
        try {
            return jsonb.type(Map.class).fromJson(reader);
        } catch (Exception e) {
            throw new DeserializationException("Failed to deserialize Reader to Map", e);
        }
    }
}
