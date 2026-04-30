package colesico.framework.telehttp.internal;

import colesico.framework.telehttp.writer.PlainTextWriter;
import colesico.framework.telehttp.writer.principal.PrincipalWriter;
import colesico.framework.telehttp.writer.profile.ProfileWriter;
import colesico.framework.ioc.production.Produce;
import colesico.framework.ioc.production.Producer;

@Producer
@Produce(PlainTextWriter.class)
@Produce(PrincipalWriter.class)
@Produce(ProfileWriter.class)
public class WritersProducer {
}
