package colesico.framework.pebble.internal;

import colesico.framework.profile.ProfileManager;
import io.pebbletemplates.pebble.extension.Function;
import io.pebbletemplates.pebble.template.EvaluationContext;
import io.pebbletemplates.pebble.template.PebbleTemplate;

import java.util.List;
import java.util.Map;

/**
 * Return current locale
 */
public class ProfileFunction implements Function {
    public static final String FUNCTION_NAME = "profile";

    private final ProfileManager profileManager;

    public ProfileFunction(ProfileManager profileManager) {
        this.profileManager = profileManager;
    }

    @Override
    public Object execute(Map<String, Object> args, PebbleTemplate self, EvaluationContext context, int lineNumber) {
        return profileManager.resolve();
    }

    @Override
    public List<String> getArgumentNames() {
        return null;
    }
}
