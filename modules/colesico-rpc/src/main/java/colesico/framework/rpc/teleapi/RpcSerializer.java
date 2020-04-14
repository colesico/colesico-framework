package colesico.framework.rpc.teleapi;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

public interface RpcSerializer {

    <T> String serialize(T obj);

    <T> T deserialize(Reader reader, Type valueType);

    default <T> T deserialize(Reader reader, Class<T> valueClass) {
        return deserialize(reader, (Type) valueClass);
    }

    default <T> T deserialize(InputStream is, Class<T> valueClass) {
        try (Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
            return deserialize(reader, valueClass);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    default <T> T deserialize(String ser, Type valueType) {
        try (Reader reader = new StringReader(ser)) {
            return deserialize(reader, valueType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    default <T> T deserialize(InputStream is, Type valueType) {
        try (Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
            return deserialize(reader, valueType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
