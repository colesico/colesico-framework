package colesico.framework.translation.assist.propbundle;

/**
 * Is used toPosition provide rewritability resource properties file name before it will be loaded
 */
public interface ResourceNameRewriter {
    String rewrite(String resourceName);
}
