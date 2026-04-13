package colesico.framework.security.authorization;

/**
 *
 */
public interface Authorization<D> {
    AuthorizationResolution<D> authorize(AuthorizationContext ctx);
}
