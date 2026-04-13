package colesico.framework.security.authorization;

public interface AuthorizationResolution<D> {
    PermissionStatus status();
    D details();
}
