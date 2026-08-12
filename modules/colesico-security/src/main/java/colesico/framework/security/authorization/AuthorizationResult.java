package colesico.framework.security.authorization;

public interface AuthorizationResult {

    PermissionStatus status();

    Object details();

    default boolean isGranted() {
        return PermissionStatus.GRANTED.equals(status());
    }

    default boolean isDenied() {
        return PermissionStatus.DENIED.equals(status());
    }

    default boolean isUndefined() {
        return PermissionStatus.UNDEFINED.equals(status());
    }

    record ImmutableAuthorizationResult(PermissionStatus status,
                                        Object details) implements AuthorizationResult {

    }

    static ImmutableAuthorizationResult granted(Object details) {
        return new ImmutableAuthorizationResult(PermissionStatus.GRANTED, details);
    }

    static ImmutableAuthorizationResult granted() {
        return new ImmutableAuthorizationResult(PermissionStatus.GRANTED, null);
    }

    static ImmutableAuthorizationResult denied(Object details) {
        return new ImmutableAuthorizationResult(PermissionStatus.DENIED, details);
    }

    static ImmutableAuthorizationResult denied() {
        return new ImmutableAuthorizationResult(PermissionStatus.DENIED, null);
    }

    static ImmutableAuthorizationResult undefined(Object details) {
        return new ImmutableAuthorizationResult(PermissionStatus.UNDEFINED, details);
    }

    static ImmutableAuthorizationResult undefined() {
        return new ImmutableAuthorizationResult(PermissionStatus.UNDEFINED, null);
    }
}
