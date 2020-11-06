package colesico.framework.resource;

/**
 * Rewrites the given path.
 * Rewriter can be used to localize, rewrite resources e.t.c
 */
public interface PathRewriter {

    /**
     * Return rewriting phase to apply rewriter
     */
    RewritingPhase phase();

    /**
     * Rewrite path
     */
    String rewrite(String path);
}
