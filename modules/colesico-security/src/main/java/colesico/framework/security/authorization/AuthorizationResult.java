package colesico.framework.security.authorization;

public interface AuthorizationResult<D> {

    PermissionStatus status();

    D details();

    default boolean isGranted() {
        return PermissionStatus.GRANTED.equals(status());
    }

    default boolean isDenied() {
        return PermissionStatus.DENIED.equals(status());
    }

    default boolean isUndefined() {
        return PermissionStatus.UNDEFINED.equals(status());
    }

    record ImmutableAuthorizationResult<D>(PermissionStatus status,
                                           D details) implements AuthorizationResult<D> {

    }

    static <D> ImmutableAuthorizationResult<D> granted(D details) {
        return new ImmutableAuthorizationResult<>(PermissionStatus.GRANTED, details);
    }

    static <D> ImmutableAuthorizationResult<D> granted() {
        return new ImmutableAuthorizationResult<>(PermissionStatus.GRANTED, null);
    }

    static <D> ImmutableAuthorizationResult<D> denied(D details) {
        return new ImmutableAuthorizationResult<>(PermissionStatus.DENIED, details);
    }

    static <D> ImmutableAuthorizationResult<D> denied() {
        return new ImmutableAuthorizationResult<>(PermissionStatus.DENIED, null);
    }

    static <D> ImmutableAuthorizationResult<D> undefined(D details) {
        return new ImmutableAuthorizationResult<>(PermissionStatus.UNDEFINED, details);
    }

    static <D> ImmutableAuthorizationResult<D> undefined() {
        return new ImmutableAuthorizationResult<>(PermissionStatus.UNDEFINED, null);
    }
}
