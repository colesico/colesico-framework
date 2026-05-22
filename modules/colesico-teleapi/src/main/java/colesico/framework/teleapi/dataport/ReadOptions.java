package colesico.framework.teleapi.dataport;

/**
 * Represents a generic options for reading data from channel with {@link TeleReader}
 */
public abstract class ReadOptions<A> {

    /**
     * Any custom data
     */
    protected final A attachment;

    public ReadOptions(A attachment) {
        this.attachment = attachment;
    }

    /**
     * Returns the custom data attached to these options
     */
    public A attachment() {
        return attachment;
    }
}
