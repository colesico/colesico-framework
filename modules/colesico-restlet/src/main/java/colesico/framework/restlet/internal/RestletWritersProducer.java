package colesico.framework.restlet.internal;

import colesico.framework.ioc.Classed;
import colesico.framework.ioc.Producer;
import colesico.framework.profile.Profile;
import colesico.framework.restlet.teleapi.RestletTeleWriter;
import colesico.framework.restlet.teleapi.RestletWriterProxy;
import colesico.framework.security.Principal;
import colesico.framework.weblet.HtmlResponse;
import colesico.framework.weblet.NavigationResponse;
import colesico.framework.weblet.TextResponse;
import colesico.framework.weblet.VariousResponse;
import colesico.framework.weblet.teleapi.writer.*;

import javax.inject.Singleton;


@Producer
public class RestletWritersProducer {

    @Singleton
    @Classed(TextResponse.class)
    public RestletTeleWriter getTextResponseWriter(StringWriter impl) {
        return new RestletWriterProxy(impl);
    }

    @Singleton
    @Classed(HtmlResponse.class)
    public RestletTeleWriter getHtmlResponseWriter(StringWriter impl) {
        return new RestletWriterProxy(impl);
    }

    @Singleton
    @Classed(NavigationResponse.class)
    public RestletTeleWriter getNavigationResponseWriter(NavigationWriter impl) {
        return new RestletWriterProxy(impl);
    }

    @Singleton
    @Classed(VariousResponse.class)
    public RestletTeleWriter getVariousResponseWriter(VariousWriter impl) {
        return new RestletWriterProxy(impl);
    }

    @Singleton
    @Classed(Principal.class)
    public RestletTeleWriter getPrincipalWriter(PrincipalWriter impl) {
        return new RestletWriterProxy(impl);
    }

    @Singleton
    @Classed(Profile.class)
    public RestletTeleWriter getProfileWriter(ProfileWriter impl) {
        return new RestletWriterProxy(impl);
    }
}
