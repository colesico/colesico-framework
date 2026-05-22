package colesico.framework.teleapi.dataport;

/**
 * Represents a generic options for writing data to channel with {@link TeleWriter}
 */
abstract public class WriteOptions<A> {

    /**
     * Any custom data
     */
    protected final A attachment;

    public WriteOptions(A attachment) {
        this.attachment = attachment;
    }

    /**
     * Returns the custom data attached to these options.
     */
    public A attachment() {
        return attachment;
    }

}
