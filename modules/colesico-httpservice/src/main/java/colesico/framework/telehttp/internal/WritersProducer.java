package colesico.framework.telehttp.internal;

import colesico.framework.telehttp.readwrite.PlainTextWriter;
import colesico.framework.telehttp.readwrite.principal.PrincipalWriter;
import colesico.framework.telehttp.readwrite.profile.ProfileWriter;
import colesico.framework.ioc.production.Produce;
import colesico.framework.ioc.production.Producer;

@Producer
@Produce(PlainTextWriter.class)
@Produce(PrincipalWriter.class)
@Produce(ProfileWriter.class)
public class WritersProducer {
}
