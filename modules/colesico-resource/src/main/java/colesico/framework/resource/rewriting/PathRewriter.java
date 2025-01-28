package colesico.framework.resource.rewriting;

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
     * Phase of path rewriting  in which this rewriter will be called
     */
    RewritingPhase phase();
}
