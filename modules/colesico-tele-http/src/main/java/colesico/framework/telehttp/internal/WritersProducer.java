package colesico.framework.telehttp.internal;

import colesico.framework.ioc.conditional.Substitution;
import colesico.framework.telehttp.writer.ExceptionWriter;
import colesico.framework.telehttp.writer.PlainTextWriter;
import colesico.framework.telehttp.writer.ProfileWriter;
import colesico.framework.ioc.production.Produce;
import colesico.framework.ioc.production.Producer;

@Producer
@Produce(PlainTextWriter.class)
@Produce(ExceptionWriter.class)
@Produce(value = ProfileWriter.class, substitute = Substitution.STUB)
public class WritersProducer {
}
