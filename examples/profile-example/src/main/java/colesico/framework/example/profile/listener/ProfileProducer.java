package colesico.framework.example.profile.listener;

import colesico.framework.ioc.production.Produce;
import colesico.framework.ioc.production.Producer;
import colesico.framework.profile.ProfileListener;

@Producer
@Produce(value = CustomProfileListener.class, keyType = ProfileListener.class)
public class ProfileProducer {
}
