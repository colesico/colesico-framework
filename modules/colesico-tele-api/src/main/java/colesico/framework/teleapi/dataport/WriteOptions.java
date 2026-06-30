package colesico.framework.teleapi.dataport;

import java.lang.reflect.Type;

/**
 * Represents a generic options for writing data to channel with {@link TeleWriter}
 */
public interface WriteOptions {

    /**
     * Writing value base type
     */
    Type baseType();

    /**
     * Returns the custom data attached to these options.
     */
    Object metadata();

    default <T> T attachmentAs(Class<T> type) {
        return type.cast(metadata());
    }
}
