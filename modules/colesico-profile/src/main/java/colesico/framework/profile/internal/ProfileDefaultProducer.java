package colesico.framework.profile.internal;

import colesico.framework.ioc.conditional.Substitute;
import colesico.framework.ioc.production.Produce;
import colesico.framework.ioc.production.Producer;
import colesico.framework.profile.ProfileListener;

import static colesico.framework.ioc.conditional.Substitution.STUB;

@Producer
@Substitute(STUB)
@Produce(value = DefaultProfileListener.class, keyType = ProfileListener.class)
public class ProfileDefaultProducer {
}
