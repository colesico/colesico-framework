package colesico.framework.teleapi.dataport;

/**
 * Represents a generic options for reading data from channel with {@link TeleReader}
 */
public interface ReadOptions {

    /**
     * Returns the custom data attached to these options
     */
    Object attachment();

    default <T> T attachmentAs(Class<T> type) {
        return type.cast(attachment());
    }
}
