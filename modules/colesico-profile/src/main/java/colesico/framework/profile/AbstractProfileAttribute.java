package colesico.framework.profile;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Profile attribute common accessor
 */
abstract public class AbstractProfileAttribute<P extends Profile, V>
        implements ProfileAttribute<P, V> {

    protected final P profile;
    protected final String name;
    protected final Map<String, Object> metadata;

    public AbstractProfileAttribute(P profile, String name, Map<String, Object> metadata) {
        this.profile = profile;
        this.name = name;
        this.metadata = Objects.requireNonNullElseGet(metadata, HashMap::new);
    }

    @Override
    public P profile() {
        return profile;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public Map<String, Object> metadata() {
        return metadata;
    }

    @Override
    public byte[] getBytes() {
        var value = getString();
        if (value == null) {
            return null;
        }
        return value.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public void setBytes(byte[] bytes) {
        if (bytes == null) {
            return;
        }
        setString(new String(bytes, StandardCharsets.UTF_8));
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof AbstractProfileAttribute<?, ?> that)) return false;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Override
    public String toString() {
        return "ProfileAttribute{" +
                "name='" + name + '\'' +
                '}';
    }
}
