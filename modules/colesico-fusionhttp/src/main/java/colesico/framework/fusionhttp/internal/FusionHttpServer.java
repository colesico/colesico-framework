package colesico.framework.fusionhttp.internal;

import colesico.framework.fusionhttp.FusionHttpConfigPrototype;
import colesico.framework.httpserver.HttpServer;
import io.fusionauth.http.server.HTTPServer;
import io.fusionauth.http.server.HTTPServerConfiguration;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class FusionHttpServer implements HttpServer{

    private final HTTPServer server;

    @Inject
    public FusionHttpServer(FusionHttpConfigPrototype config,
                            FusionHttpHandler handler) {
        HTTPServerConfiguration configuration = new HTTPServerConfiguration();
        config.applyConfiguration(configuration);
        configuration.withHandler(handler);
        server = new HTTPServer().withConfiguration(configuration);
    }

    @Override
    public HttpServer start() {
        server.start();
        return this;
    }

    @Override
    public void stop() {
        server.close();
    }
}
