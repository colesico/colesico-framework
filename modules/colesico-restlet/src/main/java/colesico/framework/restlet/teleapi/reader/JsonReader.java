package colesico.framework.restlet.teleapi.reader;

import colesico.framework.http.HttpContext;
import colesico.framework.http.HttpMethod;
import colesico.framework.http.HttpRequest;
import colesico.framework.restlet.teleapi.RestletJsonConverter;
import colesico.framework.restlet.teleapi.RestletTRContext;
import colesico.framework.telehttp.Origin;
import colesico.framework.telehttp.OriginFactory;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.io.InputStream;

import static colesico.framework.http.HttpMethod.*;

@Singleton
public final class JsonReader implements ValueReader {

    private final RestletJsonConverter jsonConverter;
    private final Provider<HttpContext> httpContextProv;
    private final OriginFactory originFactory;

    @Inject
    public JsonReader(RestletJsonConverter jsonConverter, Provider<HttpContext> httpContextProv, OriginFactory originFactory) {
        this.jsonConverter = jsonConverter;
        this.httpContextProv = httpContextProv;
        this.originFactory = originFactory;
    }

    @Override
    public Object read(RestletTRContext context) {
        HttpRequest request = httpContextProv.get().getRequest();

        HttpMethod requestMethod = request.getRequestMethod();

        // Should the value be read from input stream?
        String originName = context.getOriginName();

        boolean useInputStream = originName.equals(Origin.BODY) ||
                (
                        originName.equals(Origin.AUTO) &&
                                (
                                        requestMethod.is(POST)
                                                || requestMethod.is(PUT)
                                                || requestMethod.is(PATCH)
                                )
                );

        if (useInputStream) {
            try (InputStream is = request.getInputStream()) {
                return jsonConverter.fromJson(is, context.getValueType());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                Origin<String, String> origin = (Origin<String, String>) originFactory.getOrigin(context.getOriginName());
                String strValue = origin.getValue(context.getName());
                if (StringUtils.isBlank(strValue)) {
                    return null;
                }
                return jsonConverter.fromJson(strValue, context.getValueType());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
