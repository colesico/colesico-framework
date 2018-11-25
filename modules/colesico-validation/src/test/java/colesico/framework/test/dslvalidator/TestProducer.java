package colesico.framework.test.dslvalidator;

import colesico.framework.ioc.Produce;
import colesico.framework.ioc.Producer;
import colesico.framework.ioc.Rank;
import colesico.framework.profile.DefaultProfile;
import colesico.framework.profile.Profile;

import javax.inject.Singleton;
import java.util.Locale;

@Producer(Rank.RANK_TEST)
@Produce(MyValidatorBuilder.class)
public class TestProducer {

    @Singleton
    public Profile getProfile() {
        return new DefaultProfile(new Locale("en","RU"));
    }
}
