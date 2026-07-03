package colesico.framework.restlet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Core contract for JSON serialization and deserialization within the REST framework.
 * High-performance I/O operations rely on streams, while string-based utilities
 * encapsulate the underlying byte conversions using UTF-8 standard.
 */
public interface JsonSerializer {

    /**
     * The standard character encoding for JSON data interchange as per RFC 8259.
     */
    Charset JSON_CHARSET = StandardCharsets.UTF_8;

    /**
     * Serializes an object directly into the provided output stream.
     *
     * @param value        The object to serialize (can be null)
     * @param baseType     The explicit type of the object, supporting generics
     * @param charset      The character encoding for the stream; if null, {@link #JSON_CHARSET} is used
     * @param outputStream The target stream to write the JSON payload to
     */
    void serialize(Object value, Type baseType, Charset charset, OutputStream outputStream);

    /**
     * Deserializes an object of the specified type from the input stream.
     *
     * @param targetType  The explicit target type, supporting generics (e.g., List&lt;DTO&gt;)
     * @param charset     The character encoding of the incoming stream; if null, {@link #JSON_CHARSET} is used
     * @param inputStream The source stream containing the JSON payload
     * @param <T>         The expected return type
     * @return The deserialized object instance, or null if the stream is empty
     */
    <T> T deserialize(InputStream inputStream, Charset charset, Type targetType);

    /**
     * Serializes an object into a standard Java String using UTF-8 byte conversion internally.
     *
     * @param value    The object to serialize (can be null)
     * @param baseType The explicit type of the object, supporting generics
     * @return The JSON string representation, or null if the input value is null
     */
    default String toString(Object value, Type baseType) {
        if (value == null) {
            return null;
        }
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            serialize(value, baseType, JSON_CHARSET, outputStream);
            return outputStream.toString(JSON_CHARSET);
        } catch (RestletException e) {
            throw e;
        } catch (Exception e) {
            throw RestletException.of(500, e);
        }
    }

    /**
     * Deserializes an object from a standard Java String using UTF-8 byte conversion internally.
     *
     * @param targetType The explicit target type, supporting generics
     * @param json       The JSON string to parse (can be null or blank)
     * @param <T>        The expected return type
     * @return The deserialized object instance, or null if the string is null or blank
     */
    default <T> T fromString(Type targetType, String json) {
        if (json == null || json.isBlank()) {
            return null;
        }
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(json.getBytes(JSON_CHARSET))) {
            return deserialize(inputStream, JSON_CHARSET, targetType);
        } catch (RestletException e) {
            throw e;
        } catch (Exception e) {
            throw RestletException.of(500, e);
        }
    }
}