package colesico.framework.security;

import java.util.Map;
import java.util.Objects;

public final class DefaultIdentity<I> implements Identity<I> {

    private final I id;

    private final  Map<String, Object> claims;

    public DefaultIdentity(I id, Map<String, Object> claims) {
        this.id = id;
        this.claims = claims;
    }

    @Override
    public I id() {
        return id;
    }

    @Override
    public Map<String, Object> claims() {
        return claims;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DefaultIdentity<?> that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Identity{" +
                "id=" + id +
                ", claims=" + claims +
                '}';
    }
}
