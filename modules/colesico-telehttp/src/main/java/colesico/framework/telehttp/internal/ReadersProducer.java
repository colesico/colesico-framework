package colesico.framework.telehttp.internal;

import colesico.framework.telehttp.reader.*;
import colesico.framework.ioc.production.Produce;
import colesico.framework.ioc.production.Producer;

@Producer
@Produce(BooleanReader.class)
@Produce(StringReader.class)
@Produce(CharacterReader.class)
@Produce(ByteReader.class)
@Produce(ShortReader.class)
@Produce(IntegerReader.class)
@Produce(OptionalIntReader.class)
@Produce(LongReader.class)
@Produce(OptionalLongReader.class)
@Produce(FloatReader.class)
@Produce(DoubleReader.class)
@Produce(DateReader.class)
@Produce(LocalDateReader.class)
@Produce(LocalTimeReader.class)
@Produce(LocalDateTimeReader.class)
@Produce(PrincipalReader.class)
@Produce(ProfileReader.class)
public class ReadersProducer {
}
