package colesico.framework.config.internal;

import colesico.framework.config.PropertiesSource;
import colesico.framework.ioc.Produce;
import colesico.framework.ioc.Producer;

@Producer
@Produce(PropertiesSource.class)
public class ConfigProducer {
}
