package colesico.framework.restlet.internal;

import colesico.framework.restlet.RestletConfigPrototype;

/**
 * Default restlet config
 */
public class RestletConfigImpl extends RestletConfigPrototype {
    @Override
    public Boolean enableCSFRProtection() {
        return Boolean.FALSE;
    }
}
