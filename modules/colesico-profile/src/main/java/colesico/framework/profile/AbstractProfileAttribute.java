package colesico.framework.profile;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * Profile attribute accessor
 */
abstract public class AbstractProfileAttribute<P extends Profile, V> implements ProfileAttribute<V> {

    protected final P profile;
    protected Metadata metadata;

    public AbstractProfileAttribute(P profile) {
        this.profile = profile;
        initMetadata();
    }

    /**
     * Override this method to change profile attribute metainformation
     */
    protected void initMetadata() {
        this.metadata = new Metadata();
        metadata.dataPortWritable = true;
        metadata.dataPortReadable = true;
        metadata.storageReadable = false;
        metadata.storageWritable = false;
    }

    public P getProfile() {
        return profile;
    }

    @Override
    public Metadata metadata() {
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
        if (!(o instanceof ProfileAttribute<?> that)) return false;
        return Objects.equals(name(), that.name());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name());
    }
}
