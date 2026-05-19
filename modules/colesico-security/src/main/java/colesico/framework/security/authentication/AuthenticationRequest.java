package colesico.framework.security.authentication;

import colesico.framework.ioc.Ioc;
import colesico.framework.ioc.key.NamedKey;

import java.util.Map;
import java.util.Optional;

/**
 * Authentication request
 */
public interface AuthenticationRequest {

    /**
     * Specifies the unique name for the {@link NamedKey} used to retrieve the
     * {@link AuthenticationSource} implementation from the {@link Ioc} container.
     * The name is used to route security actions, such as logout, to the correct source.
     */
    String SOURCE_CLAIM = "source";

    /**
     * Returns a map of all claims associated with this request.
     */
    Map<String, Object> claims();

    /**
     * Retrieves a claim by its key and casts it to the specified type.
     */
    default <T> Optional<T> claim(String key, Class<T> type) {
        Object value = claims().get(key);
        return type.isInstance(value) ? Optional.of(type.cast(value)) : Optional.empty();
    }

    /**
     * Syntactic sugar for retrieving a typed claim with a default value.
     */
    default <T> T claimOrElse(String key, Class<T> type, T defaultValue) {
        return claim(key, type).orElse(defaultValue);
    }
}
