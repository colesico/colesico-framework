package colesico.framework.resource;

/**
 * Rewrites the given path.
 * Rewriter can be used to  localize, rewrite resources etc.
 */
public interface PathRewriter {

    /**
     * Rewrite path
     */
    String rewrite(String path);

    /**
     * Phases of path rewriting  in which this rewriter will be called
     */
    RewritingPhase[] phases();
}
