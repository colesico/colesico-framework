package colesico.framework.security.authorization;

public interface AuthorizationStrategy {
    AuthorizationResult<?> authorize(AuthorizationContext ctx);
}
