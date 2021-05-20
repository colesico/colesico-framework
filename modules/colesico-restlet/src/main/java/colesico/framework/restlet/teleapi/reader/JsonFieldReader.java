package colesico.framework.restlet.teleapi.reader;

import colesico.framework.restlet.RestletError;
import colesico.framework.restlet.RestletException;
import colesico.framework.restlet.teleapi.RestletTRContext;
import colesico.framework.restlet.teleapi.jsonrequest.JsonField;
import colesico.framework.restlet.teleapi.jsonrequest.JsonRequest;

import javax.inject.Provider;
import javax.inject.Singleton;

/**
 * @see JsonField
 */
@Singleton
public final class JsonFieldReader implements ValueReader {

    private final Provider<JsonRequest> jsonRequestProv;

    public JsonFieldReader(Provider<JsonRequest> jsonRequestProv) {
        this.jsonRequestProv = jsonRequestProv;
    }

    @Override
    public Object read(RestletTRContext context) {
        JsonRequest jr = jsonRequestProv.get();
        if (jr == null) {
            throw new RestletException(new RestletError("JsonRequestNotFound", "Json request not found", null));
        }

        RestletTRContext.JsonFieldGetter getter = context.getFieldGetter();
        if (getter == null) {
            throw new RestletException(new RestletError("JsonFieldGetterNotFound", "Json field getter not found", null));
        }

        return getter.get(jr);
    }

}
