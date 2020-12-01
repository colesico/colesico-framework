package colesico.framework.rpc.transport.http;

import colesico.framework.config.ConfigModel;
import colesico.framework.config.ConfigPrototype;

import java.net.http.HttpClient;

@ConfigPrototype(model = ConfigModel.POLYVARIANT)
abstract public class HttpRpcClientOptionsPrototype {
    abstract public void applyOptions(HttpClient.Builder httpClientBuilder);
}
