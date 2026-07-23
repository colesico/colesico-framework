package colesico.framework.pebble.internal;

import colesico.framework.profile.ProfileManager;
import colesico.framework.security.SecurityManager;
import io.pebbletemplates.pebble.extension.Function;
import io.pebbletemplates.pebble.template.EvaluationContext;
import io.pebbletemplates.pebble.template.PebbleTemplate;

import java.util.List;
import java.util.Map;

/**
 * Return current locale
 */
public class IdentityFunction implements Function {
    public static final String FUNCTION_NAME = "identity";

    private final SecurityManager securityManager;

    public IdentityFunction(SecurityManager securityManager) {
        this.securityManager = securityManager;
    }

    @Override
    public Object execute(Map<String, Object> args, PebbleTemplate self, EvaluationContext context, int lineNumber) {
        return securityManager.identity().orElse(null);
    }

    @Override
    public List<String> getArgumentNames() {
        return null;
    }
}
