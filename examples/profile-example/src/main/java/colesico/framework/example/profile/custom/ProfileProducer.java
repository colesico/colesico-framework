package colesico.framework.example.profile.custom;

import colesico.framework.example.profile.listener.CustomProfileListener;
import colesico.framework.ioc.production.Produce;
import colesico.framework.ioc.production.Producer;
import colesico.framework.profile.ProfileFactory;
import colesico.framework.profile.ProfileListener;

@Producer
@Produce(value = CustomProfileFactory.class, keyType = ProfileFactory.class)
public class ProfileProducer {
}
