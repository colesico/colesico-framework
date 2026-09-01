package colesico.framework.telehttp.internal;

import colesico.framework.ioc.production.Produce;
import colesico.framework.ioc.production.Producer;
import colesico.framework.telehttp.authentication.HttpBasic;
import colesico.framework.telehttp.authentication.HttpBasicClient;
import colesico.framework.telehttp.origin.OriginFactory;
import colesico.framework.telehttp.assist.CSRFProtector;

@Producer
@Produce(CSRFProtector.class)
@Produce(OriginFactory.class)
@Produce(HttpBasic.class)
@Produce(HttpBasicClient.class)
public class TeleHttpProducer {

}
