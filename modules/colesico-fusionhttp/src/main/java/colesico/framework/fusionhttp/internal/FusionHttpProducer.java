package colesico.framework.fusionhttp.internal;

import colesico.framework.fusionhttp.FusionHttpConfigPrototype;
import colesico.framework.http.HttpCookieFactory;
import colesico.framework.httpserver.HttpServer;
import colesico.framework.ioc.production.Produce;
import colesico.framework.ioc.production.Producer;
import io.fusionauth.http.server.HTTPListenerConfiguration;
import io.fusionauth.http.server.HTTPServerConfiguration;

@Producer
@Produce(value = FusionHttpServer.class, keyType = HttpServer.class)
@Produce(FusionHttpHandler.class)
@Produce(value = FusionHttpCookieFactory.class, keyType = HttpCookieFactory.class)
public class FusionHttpProducer {

    public FusionHttpConfigPrototype configPrototype() {
        return new FusionHttpConfigPrototype() {
            @Override
            public void applyConfiguration(HTTPServerConfiguration config) {
                config.withListener(new HTTPListenerConfiguration(8080));
            }
        };
    }
}
