package colesico.framework.example.profile.custom;

import colesico.framework.ioc.production.Produce;
import colesico.framework.ioc.production.Producer;
import colesico.framework.profile.ProfileManager;
import colesico.framework.profile.ProfileSource;

@Producer
@Produce(value = CustomProfileSource.class, keyType = ProfileSource.class)
public class ProfileProducer {
}
