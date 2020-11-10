package colesico.framework.translation.assist.propbundle;

/**
 * Is used to provide rewritability resource properties file name before it will be loaded
 */
public interface ResourceNameRewriter {
    String rewrite(String resourceName);
}
