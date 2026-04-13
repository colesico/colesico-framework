package colesico.framework.security.authorization;

public record DefaultAuthorizationResult(PermissionStatus status, String details)
        implements AuthorizationResult<String> {
}
