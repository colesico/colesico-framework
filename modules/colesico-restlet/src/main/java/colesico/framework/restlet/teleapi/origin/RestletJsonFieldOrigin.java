package colesico.framework.restlet.teleapi.origin;

import colesico.framework.restlet.teleapi.RestletOrigin;
import org.apache.commons.lang3.NotImplementedException;

/**
 * Origin to retrieve string value from http request body json field
 */
public class RestletJsonFieldOrigin implements RestletOrigin {

    @Override
    public String getString(String name) {
        throw new NotImplementedException("Json field origin not implemented yet");
    }
}
