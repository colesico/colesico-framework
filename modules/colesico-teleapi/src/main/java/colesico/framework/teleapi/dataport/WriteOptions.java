package colesico.framework.teleapi.dataport;

/**
 * Represents a generic options for writing data to channel with {@link TeleWriter}
 */
public interface WriteOptions<A> {

    /**
     * Returns the custom data attached to these options.
     */
    A attachment();

}
