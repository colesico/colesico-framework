package colesico.framework.example.profile.custom;

import colesico.framework.ioc.production.Produce;
import colesico.framework.ioc.production.Producer;

@Producer
@Produce(value = CustomProfileManagerImpl.class, keyType = ProfileManager.class)
public class ProfileProducer {
}
