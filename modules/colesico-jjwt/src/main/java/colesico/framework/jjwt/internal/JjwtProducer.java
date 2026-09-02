package colesico.framework.jjwt.internal;

import colesico.framework.ioc.production.Produce;
import colesico.framework.ioc.production.Producer;
import colesico.framework.jjwt.HttpJwtClient;
import colesico.framework.jjwt.Jwt;
import colesico.framework.jjwt.JwtClient;
import colesico.framework.jjwt.JwtTokenUtils;

@Producer
@Produce(Jwt.class)
@Produce(value = HttpJwtClient.class, keyType = JwtClient.class)
@Produce(JwtTokenUtils.class)
public class JjwtProducer {
}
