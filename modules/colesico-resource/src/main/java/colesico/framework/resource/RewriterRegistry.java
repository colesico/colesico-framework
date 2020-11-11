package colesico.framework.resource;

/**
 * Used to attach path rewriters to resource kit
 */
public interface RewriterRegistry {

    /**
     * Register rewriter in the register.
     * Throws an exception if rewriter with given id already exists
     */
    void register(String id, PathRewriter rewriter, RewritingPhase phase);

    /**
     * Registers rewriter in the register if it is not been registered before
     */
    void registerIfAbsent(String id, PathRewriter rewriter, RewritingPhase phase);

    /**
     * Register rewriter with random uniquie id;
     */
    void register(PathRewriter rewriter, RewritingPhase phase);

    Entry get(String id);

    final class Entry {

        private final PathRewriter rewriter;
        private final RewritingPhase phase;

        public Entry(PathRewriter rewriter, RewritingPhase phase) {
            this.rewriter = rewriter;
            this.phase = phase;
        }

        public PathRewriter getRewriter() {
            return rewriter;
        }

        public RewritingPhase getPhase() {
            return phase;
        }
    }
}
