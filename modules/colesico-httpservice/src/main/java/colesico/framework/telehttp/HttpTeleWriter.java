package colesico.framework.telehttp;

import colesico.framework.http.HttpResponse;
import colesico.framework.teleapi.dataport.TeleWriter;

/**
 * Basic writer for interaction over http
 */
@FunctionalInterface
public interface HttpTeleWriter<V, W extends HttpWriteOptions> extends TeleWriter<V, W, HttpTeleWriter.Channel> {

    /**
     * Write channel api
     */
    record Channel(HttpResponse httpResponse) {
    }

}
