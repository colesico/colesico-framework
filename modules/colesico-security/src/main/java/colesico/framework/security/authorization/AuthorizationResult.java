package colesico.framework.security.authorization;

public interface AuthorizationResult<D> {
    PermissionStatus status();
    D details();
}
