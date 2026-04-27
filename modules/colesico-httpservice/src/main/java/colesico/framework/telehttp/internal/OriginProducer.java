package colesico.framework.telehttp.internal;

import colesico.framework.ioc.production.Produce;
import colesico.framework.ioc.production.Producer;
import colesico.framework.telehttp.Origin;
import colesico.framework.telehttp.origin.*;

@Producer
@Produce(value = BodyOrigin.class, keyType = Origin.class, named = Origin.BODY)
@Produce(value = CookieOrigin.class, keyType = Origin.class, named = Origin.COOKIE)
@Produce(value = HeaderOrigin.class, keyType = Origin.class, named = Origin.HEADER)
@Produce(value = PostOrigin.class, keyType = Origin.class, named = Origin.POST)
@Produce(value = QueryOrigin.class, keyType = Origin.class, named = Origin.QUERY)
@Produce(value = RouteOrigin.class, keyType = Origin.class, named = Origin.ROUTE)
public class OriginProducer {
}
