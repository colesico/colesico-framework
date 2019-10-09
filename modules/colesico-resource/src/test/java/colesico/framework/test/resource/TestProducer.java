package colesico.framework.test.resource;

import colesico.framework.ioc.Polyproduce;
import colesico.framework.ioc.Producer;
import colesico.framework.ioc.Rank;
import colesico.framework.profile.DefaultProfile;
import colesico.framework.profile.Profile;
import colesico.framework.resource.ResourceOptionsPrototype;

import javax.inject.Singleton;
import java.util.Locale;

@Producer(Rank.RANK_TEST)
public class TestProducer {

    @Singleton
    public Profile getProfile() {
        return new DefaultProfile(new Locale("en","RU"));
    }

    @Singleton
    @Polyproduce
    public ResourceOptionsPrototype getResourceConfig() {
        return new ResourcesConf();
    }
}

