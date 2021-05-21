package colesico.framework.restlet.internal;

import colesico.framework.http.HttpRequest;
import colesico.framework.restlet.teleapi.RestletJsonConverter;
import colesico.framework.restlet.teleapi.jsonrequest.JsonRequest;

import javax.inject.Provider;
import javax.inject.Singleton;
import java.io.InputStream;

@Singleton
public class JsonRequestFactory {

    private final RestletJsonConverter jsonConverter;
    private final Provider<HttpRequest> httpRequestProv;

    public JsonRequestFactory(RestletJsonConverter jsonConverter, Provider<HttpRequest> httpRequestProv) {
        this.jsonConverter = jsonConverter;
        this.httpRequestProv = httpRequestProv;
    }

    public <V extends JsonRequest> V getJsonRequest(Class<V> jsonRequestType) {
        HttpRequest request = httpRequestProv.get();
        try (InputStream is = request.getInputStream()) {
            return jsonConverter.fromJson(is, jsonRequestType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
