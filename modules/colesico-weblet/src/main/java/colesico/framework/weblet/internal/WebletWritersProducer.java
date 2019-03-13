package colesico.framework.weblet.internal;

import colesico.framework.ioc.Classed;
import colesico.framework.ioc.Produce;
import colesico.framework.ioc.Producer;
import colesico.framework.ioc.Rank;
import colesico.framework.profile.Profile;
import colesico.framework.security.Principal;
import colesico.framework.weblet.*;
import colesico.framework.weblet.teleapi.WebletTeleWriter;
import colesico.framework.weblet.teleapi.writer.*;

import javax.inject.Singleton;

@Producer(Rank.RANK_MINOR)
@Produce(StringWriter.class)
@Produce(NavigationWriter.class)
@Produce(BinaryWriter.class)
@Produce(VariousWriter.class)
@Produce(PrincipalWriter.class)
@Produce(ProfileWriter.class)
public class WebletWritersProducer {

    @Singleton
    @Classed(BinaryResponse.class)
    public WebletTeleWriter getBinaryWriter(BinaryWriter impl) {
        return impl;
    }

    @Singleton
    @Classed(TextResponse.class)
    public WebletTeleWriter getTextResponseWriter(StringWriter impl) {
        return impl;
    }

    @Singleton
    @Classed(HtmlResponse.class)
    public WebletTeleWriter getHtmlResponseWriter(StringWriter impl) {
        return impl;
    }

    @Singleton
    @Classed(NavigationResponse.class)
    public WebletTeleWriter getNavigationResponseWriter(NavigationWriter impl) {
        return impl;
    }

    @Singleton
    @Classed(VariousResponse.class)
    public WebletTeleWriter getVariousResponseWriter(VariousWriter impl) {
        return impl;
    }

    @Singleton
    @Classed(Principal.class)
    public WebletTeleWriter getPrincipalWriter(PrincipalWriter impl) {
        return impl;
    }

    @Singleton
    @Classed(Profile.class)
    public WebletTeleWriter getProfileWriter(ProfileWriter impl) {
        return impl;
    }
}
