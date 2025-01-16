package colesico.framework.example.profile.custom;

import colesico.framework.ioc.production.Produce;
import colesico.framework.ioc.production.Producer;
import colesico.framework.profile.ProfileValueUtils;

@Producer
@Produce(value = TimezoneValueUtils.class, keyType = ProfileValueUtils.class, polyproduce = true)
public class ProfileProducer {
}
