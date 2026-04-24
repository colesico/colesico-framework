package colesico.framework.restlet.teleapi.reader;

import colesico.framework.http.HttpContext;
import colesico.framework.http.HttpMethod;
import colesico.framework.http.HttpRequest;
import colesico.framework.restlet.RestletError;
import colesico.framework.restlet.RestletException;
import colesico.framework.restlet.teleapi.RestletJsonConverter;
import colesico.framework.restlet.teleapi.RestletOrigin;
import colesico.framework.restlet.teleapi.RestletTRContext;
import colesico.framework.telehttp.Origin;
import colesico.framework.telehttp.OriginFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import jakarta.inject.Inject;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;
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
        HttpRequest request = httpContextProv.get().request();

        HttpMethod requestMethod = request.requestMethod();

        // Should the value be read from request input stream?
        String originName = context.originName();

        boolean useInputStream = originName.equals(RestletOrigin.BODY) ||
                (
                        originName.equals(RestletOrigin.AUTO)
                                &&
                                (
                                        requestMethod.is(POST)
                                                || requestMethod.is(PATCH)
                                                || requestMethod.is(DELETE)
                                                || requestMethod.is(PUT)
                                )
                );

        if (useInputStream) {
            try (InputStream is = request.inputStream()) {
                return jsonConverter.fromJson(is, context.valueType());
            } catch (Exception e) {
                throw new RestletException(new RestletError("ReadJsonError", ExceptionUtils.getRootCauseMessage(e), null));
            }
        } else {
            try {
                Origin origin = originFactory.getOrigin(context.originName());
                String strValue = origin.getStrings(context.paramName());
                if (StringUtils.isBlank(strValue)) {
                    return null;
                }
                return jsonConverter.fromJson(strValue, context.valueType());
            } catch (Exception e) {
                throw new RestletException(new RestletError("ReadJsonError", ExceptionUtils.getRootCauseMessage(e), null));
            }
        }
    }
}
