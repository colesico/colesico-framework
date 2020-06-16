package colesico.framework.restlet.teleapi.writer;

import colesico.framework.http.HttpContext;
import colesico.framework.restlet.teleapi.RestletTeleWriter;

import javax.inject.Provider;

abstract public class ObjectWriter extends RestletTeleWriter<Object> {

    public ObjectWriter(Provider<HttpContext> httpContextProv) {
        super(httpContextProv);
    }

}
