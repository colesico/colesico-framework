package colesico.framework.teleapi.dataport;

/**
 * Represents a generic options for reading data from channel with {@link TeleReader}
 */
public interface ReadOptions<A> {

    /**
     * Returns the custom data attached to these options
     */
    A attachment();
}
