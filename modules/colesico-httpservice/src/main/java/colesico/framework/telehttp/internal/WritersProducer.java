package colesico.framework.telehttp.internal;

import colesico.framework.telehttp.rw.PlainTextWriter;
import colesico.framework.telehttp.rw.principal.PrincipalWriter;
import colesico.framework.telehttp.rw.profile.ProfileWriter;
import colesico.framework.ioc.production.Produce;
import colesico.framework.ioc.production.Producer;

@Producer
@Produce(PlainTextWriter.class)
@Produce(PrincipalWriter.class)
@Produce(ProfileWriter.class)
public class WritersProducer {
}
