package colesico.framework.telehttp;

import colesico.framework.http.HttpRequest;
import colesico.framework.router.RouterContext;
import colesico.framework.teleapi.dataport.TeleReader;

/**
 * Basic reader for retrieving param vales from http request
 */
@FunctionalInterface
public interface HttpTeleReader<V, R extends HttpReadOptions> extends TeleReader<V, R, HttpTeleReader.Channel> {

    /**
     * Read channel api
     */
    record Channel(HttpRequest httpRequest, RouterContext routerContext) {
    }

}
