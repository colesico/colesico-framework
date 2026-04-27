package colesico.framework.ioc.scope;

import java.util.UUID;

public interface RequestScope extends Scope{

    /**
     * Init context for  current thread.
     *  Should be called by the "dispatcher" before processing the request.
     */
    void init();

    /**
     *  Use to pass context to another thread
     */
    void init(UUID requestId);
    void destroy();

    /**
     *  Current request id
     */
    UUID requestId();
}
