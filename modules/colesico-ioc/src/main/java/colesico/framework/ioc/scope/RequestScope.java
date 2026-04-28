package colesico.framework.ioc.scope;


import java.util.UUID;

/**
 * {@link  ThreadScope#close()}  should be called by the "dispatcher" after processing the request
 */
public interface RequestScope extends Scope, AutoCloseable {

    /**
     * Init scope for  current thread.
     * Should be called by the "dispatcher" before processing the request.
     */
    void open();

    /**
     * Use to pass context to another thread
     */
    void open(UUID requestId);

    /**
     * Current request id
     */
    UUID requestId();

    @Override
    void close();
}
