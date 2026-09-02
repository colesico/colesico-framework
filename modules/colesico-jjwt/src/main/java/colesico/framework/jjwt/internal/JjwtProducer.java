package colesico.framework.jjwt.internal;

import colesico.framework.ioc.production.Produce;
import colesico.framework.ioc.production.Producer;
import colesico.framework.jjwt.*;

@Producer
@Produce(ApiJwt.class)
@Produce(ApiJwtClient.class)
@Produce(WebJwt.class)
@Produce(WebJwtClient.class)
@Produce(JwtTokenUtils.class)
public class JjwtProducer {
}
