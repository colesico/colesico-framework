package colesico.framework.teleapi.dataport;

/**
 * Represents a generic options for writing data to channel with {@link TeleWriter}
 */
public interface WriteOptions {

    /**
     * Returns the custom data attached to these options.
     */
    Object attachment();

    default <T> T attachmentAs(Class<T> type) {
        return type.cast(attachment());
    }
}
