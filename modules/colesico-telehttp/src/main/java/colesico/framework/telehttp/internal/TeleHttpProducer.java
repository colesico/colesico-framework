package colesico.framework.telehttp.internal;

import colesico.framework.ioc.production.Produce;
import colesico.framework.ioc.production.Producer;
import colesico.framework.telehttp.authentication.HttpBasicPeer;
import colesico.framework.telehttp.origin.OriginFactory;
import colesico.framework.telehttp.assist.CSRFProtector;

@Producer
@Produce(CSRFProtector.class)
@Produce(OriginFactory.class)
@Produce(HttpBasicPeer.class)
public class TeleHttpProducer {

}
