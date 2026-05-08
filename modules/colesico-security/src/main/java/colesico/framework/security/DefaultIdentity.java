package colesico.framework.security;

import java.util.Map;
import java.util.Objects;

public record DefaultIdentity<I>(I id, Map<String, Object> claims) implements Identity<I> {
}
