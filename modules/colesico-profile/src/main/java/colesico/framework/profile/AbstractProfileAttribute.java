package colesico.framework.profile;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * Profile attribute accessor
 */
abstract public class AbstractProfileAttribute<P extends Profile, V> implements ProfileAttribute<V> {

    protected final P profile;

    public AbstractProfileAttribute(P profile) {
        this.profile = profile;
    }

    public P getProfile() {
        return profile;
    }

    @Override
    public boolean readonly() {
        return false;
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
        if (!(o instanceof ProfileAttribute<?> that)) return false;
        return Objects.equals(name(), that.name());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name());
    }
}
