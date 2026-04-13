package colesico.framework.security.authorization;

public record DefaultAuthorizationResolution(PermissionStatus status, String details)
        implements AuthorizationResolution<String> {
}
