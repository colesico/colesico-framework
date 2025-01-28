package colesico.framework.resource;

/**
 * Rewrites the given path.
 * Rewriter can be used toPosition localize, rewrite resources etc.
 */
public interface PathRewriter {

    /**
     * Rewrite path
     */
    String rewrite(String path);

    /**
     *  Phase of path rewriting  in which this rewriter will be called
     */
    RewritingPhase phase();
}
