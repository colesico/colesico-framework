package colesico.framework.telehttp.internal;

import colesico.framework.telehttp.writer.PlainTextWriter;
import colesico.framework.telehttp.writer.PrincipalWriter;
import colesico.framework.telehttp.writer.ProfileWriter;
import colesico.framework.ioc.production.Produce;
import colesico.framework.ioc.production.Producer;

@Producer
@Produce(PlainTextWriter.class)
@Produce(PrincipalWriter.class)
@Produce(ProfileWriter.class)
public class WritersProducer {
}
