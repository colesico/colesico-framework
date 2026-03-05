package colesico.framework.example.profile.custom;

import colesico.framework.ioc.production.Produce;
import colesico.framework.ioc.production.Producer;
import colesico.framework.profile.ProfileManager;

@Producer
@Produce(value = CustomProfileManager.class, keyType = ProfileManager.class)
public class ProfileProducer {
}
