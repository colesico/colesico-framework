package colesico.framework.example.restlet.customwriter;

import colesico.framework.ioc.production.Produce;
import colesico.framework.ioc.production.Producer;

@Producer
@Produce(UppercaseWriter.class)
public class CustomWriterProducer {
}
