package colesico.framework.restlet.teleapi.jsonrequest;

import colesico.framework.ioc.key.Key;
import colesico.framework.ioc.key.TypeKey;

public interface JsonRequest {

    Key<JsonRequest> SCOPE_KEY = new TypeKey<>(JsonRequest.class);

}
