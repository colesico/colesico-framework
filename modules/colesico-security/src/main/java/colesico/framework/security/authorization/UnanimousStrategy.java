package colesico.framework.security.authorization;

/**
 * For critical systems.
 * If even one {@link Authorizer} denies access to a resource - access is denied.
 */
public class UnanimousStrategy implements AuthorizationStrategy{
}
