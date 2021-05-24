package colesico.framework.restlet.teleapi.reader;

import colesico.framework.restlet.teleapi.RestletTRContext;
import colesico.framework.restlet.teleapi.jsonrequest.JsonField;
import colesico.framework.restlet.teleapi.origin.JsonFieldOrigin;

import javax.inject.Singleton;

/**
 * @see JsonField
 */
@Singleton
public final class JsonFieldReader implements ValueReader {

    private final JsonFieldOrigin origin;

    public JsonFieldReader(JsonFieldOrigin origin) {
        this.origin = origin;
    }

    @Override
    public Object read(RestletTRContext context) {
        return origin.getValue(context.getJsonFieldGetter());
    }

}
