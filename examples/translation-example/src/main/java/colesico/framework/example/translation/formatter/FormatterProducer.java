package colesico.framework.example.translation.formatter;

import colesico.framework.ioc.production.Produce;
import colesico.framework.ioc.production.Producer;
import colesico.framework.translation.TextFormatter;

@Producer
@Produce(value = CustomFormatter.class, keyType = TextFormatter.class)
public class FormatterProducer {
}
