package colesico.framework.example.profile.custom;

import colesico.framework.ioc.production.Produce;
import colesico.framework.ioc.production.Producer;
import colesico.framework.profile.ProfileUtils;

@Producer
@Produce(value = CustomProfileUtils.class, keyType = ProfileUtils.class)
public class ProfileProducer {
}
