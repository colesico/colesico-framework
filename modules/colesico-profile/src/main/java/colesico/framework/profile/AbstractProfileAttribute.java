package colesico.framework.profile;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * Profile attribute common accessor
 */
abstract public class AbstractProfileAttribute<P
        extends Profile, V, M extends ProfileAttribute.Metadata>
        implements ProfileAttribute<V,M> {

    protected final P profile;
    protected final M metadata;

    public AbstractProfileAttribute(P profile, M metadata) {
        this.profile = profile;
        this.metadata = metadata;
    }

    public P profile() {
        return profile;
    }

    @Override
    public M metadata() {
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
        if (!(o instanceof ProfileAttribute<?,?> that)) return false;
        return Objects.equals(name(), that.name());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name());
    }

    public record AttributeMetadata(
            boolean readable,
            boolean writable
    ) implements Metadata {
    }
}
