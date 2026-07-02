package colesico.framework.restlet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Contract for JSON serialization and deserialization.
 */
public interface JsonConverter {

    /**
     * Serializes an object to the provided output stream.
     */
    void serialize(Object value, Type baseType, Charset charset, OutputStream outputStream);

    /**
     * Deserializes an object of the specified type from the input stream.
     *
     * @param targetType The explicit type (supports generics like List<MyDto>)
     * @param <T>        The expected return type
     */
    <T> T deserialize(Type targetType, InputStream inputStream);

    /**
     * Default implementation to serialize an object directly into a UTF-8 String.
     */
    default String toString(Object value, Type baseType, Charset charset) {
        if (value == null) {
            return null;
        }
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            serialize(value, baseType, charset, outputStream);
            return outputStream.toString(charset != null ? charset : StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw RestletException.of(e);
        }
    }

    /**
     * Default implementation to deserialize an object from a UTF-8 String.
     */
    default <T> T deserializeFromString(Type targetType, String json) throws JsonConversionException {
        if (json == null || json.isBlank()) {
            return null;
        }
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8))) {
            return deserialize(targetType, inputStream);
        } catch (JsonConversionException e) {
            throw e;
        } catch (Exception e) {
            throw new JsonConversionException("Failed to deserialize object from String", e);
        }
    }
}
