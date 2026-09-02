package colesico.framework.security.authentication;

import colesico.framework.security.Identity;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Identity issued by {@link Authenticator}
 */
public final class IssuedIdentity implements Identity {

    private final Authenticator authenticator;
    private final String id;
    private final Map<String, Object> claims;

    public IssuedIdentity(Authenticator authenticator, String id, Map<String, Object> claims) {
        Objects.requireNonNull(id, "Identity ID cannot be null");

        this.authenticator = authenticator;
        this.id = id;
        this.claims = Map.copyOf(Objects.requireNonNullElse(claims, Collections.emptyMap()));
    }

    /**
     * Returns {@link Authenticator}  that issued this identity.
     * This used to route security actions, such as logout,
     * to the correct authenticator.
     */
    public Optional<Authenticator> authenticator() {
        return Optional.ofNullable(authenticator);
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public Map<String, Object> claims() {
        return claims;
    }

    public static IssuedIdentity of(String id) {
        return new IssuedIdentity(null, id, Map.of());
    }

    public static IssuedIdentity of(Authenticator authenticator, String id) {
        return new IssuedIdentity(authenticator, id, Map.of());
    }

    public static IssuedIdentity of(Authenticator authenticator, String id, Map<String, Object> claims) {
        return new IssuedIdentity(authenticator, id, claims);
    }

    public static IssuedIdentity of(String id, Map<String, Object> claims) {
        return new IssuedIdentity(null, id, claims);
    }
}
