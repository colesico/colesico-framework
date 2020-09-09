package colesico.framework.restlet.teleapi.writer;

import colesico.framework.http.HttpContext;
import colesico.framework.http.HttpResponse;
import colesico.framework.restlet.teleapi.RestletJsonConverter;
import colesico.framework.restlet.teleapi.RestletTWContext;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

@Singleton
public final class JsonWriter extends ObjectWriter {

    public static final String JSON_CONTENT_TYPE = "application/json; charset=utf-8";

    protected final RestletJsonConverter jsonConverter;

    @Inject
    public JsonWriter(Provider<HttpContext> httpContextProv, RestletJsonConverter jsonConverter) {
        super(httpContextProv);
        this.jsonConverter = jsonConverter;
    }

    @Override
    public void write(Object value, RestletTWContext context) {
        if (value == null) {
            getResponse().sendText("", JSON_CONTENT_TYPE, 204);
            return;
        }

        String json = jsonConverter.toJson(value);
        Integer code = context.getHttpCode();
        if (code==null){
            code = 200;
        }
        getResponse().sendData(ByteBuffer.wrap(json.getBytes(StandardCharsets.UTF_8)), JSON_CONTENT_TYPE, code);
    }
}
