package colesico.framework.security.authentication;

import colesico.framework.security.Identity;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * General identity implementation
 */
public non-sealed class AuthenticatedIdentity implements Identity {

    private final Authenticator authenticator;
    private final String id;
    private final Map<String, Object> claims;

    public AuthenticatedIdentity(Authenticator authenticator, String id, Map<String, Object> claims) {
        Objects.requireNonNull(id, "Identity ID cannot be null");

        this.authenticator = authenticator;
        this.id = id;
        this.claims = Map.copyOf(Objects.requireNonNullElse(claims, Collections.emptyMap()));
    }

    /**
     * Specifies {@link Authenticator} reference that issued this identity.
     * It used to route security actions, such as logout,
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

    public static AuthenticatedIdentity of(Authenticator authenticator, String id) {
        return new AuthenticatedIdentity(authenticator, id, Map.of());
    }

    public static AuthenticatedIdentity of(Authenticator authenticator, String id, Map<String, Object> claims) {
        return new AuthenticatedIdentity(authenticator, id, claims);
    }
}
