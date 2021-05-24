package colesico.framework.restlet.teleapi.origin;

import colesico.framework.restlet.RestletError;
import colesico.framework.restlet.RestletException;
import colesico.framework.restlet.teleapi.RestletOrigin;
import colesico.framework.restlet.teleapi.RestletTRContext;
import colesico.framework.restlet.teleapi.jsonrequest.JsonRequest;

import javax.inject.Provider;
import javax.inject.Singleton;

@Singleton
public class JsonFieldOrigin implements RestletOrigin<RestletTRContext.JsonFieldGetter, Object> {

    private final Provider<JsonRequest> jsonRequestProv;

    public JsonFieldOrigin(Provider<JsonRequest> jsonRequestProv) {
        this.jsonRequestProv = jsonRequestProv;
    }

    @Override
    public Object getValue(RestletTRContext.JsonFieldGetter getter) {
        JsonRequest jr = jsonRequestProv.get();
        if (jr == null) {
            throw new RestletException(new RestletError("JsonRequestNotFound", "Json request not found", null));
        }

        if (getter == null) {
            throw new RestletException(new RestletError("JsonFieldGetterNotFound", "Json field getter not found", null));
        }

        return getter.get(jr);
    }
}
