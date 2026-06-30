package colesico.framework.teleapi.dataport;

import java.lang.reflect.Type;

/**
 * Represents a generic options for reading data from channel with {@link TeleReader}
 */
public interface ReadOptions {

    /**
     * Reading value base type
     */
    Type baseType();

    /**
     * Returns the custom data attached to these options
     */
    Object metadata();

    /**
     * Overrides the default reader to be used for reading the value
     */
    default Class<? extends TeleReader<?, ?>> customReader() {
        return null;
    }


    default <T> T metadataAs(Class<T> type) {
        return type.cast(metadata());
    }
}
