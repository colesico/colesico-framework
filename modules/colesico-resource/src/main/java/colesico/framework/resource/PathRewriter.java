package colesico.framework.resource;

/**
 * Rewrites the given path.
 * Rewriter can be used to localize, rewrite resources e.t.c
 */
@FunctionalInterface
public interface PathRewriter {

    /**
     * Rewrite path
     */
    String rewrite(String path);
}
