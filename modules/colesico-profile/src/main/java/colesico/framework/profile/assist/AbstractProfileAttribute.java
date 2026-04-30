package colesico.framework.profile.assist;

import colesico.framework.profile.Profile;

import java.nio.charset.StandardCharsets;
import java.util.Objects;



/**
 * Profile attribute helper basic implementation
 */
abstract public class AbstractProfileAttribute<P extends Profile, V>
        implements ProfileAttribute<P, V> {

    protected final P profile;
    protected final String name;

    public AbstractProfileAttribute(P profile, String name) {
        this.profile = profile;
        this.name = name;
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
    public byte[] asBytes() {
        var value = asString();
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
