package colesico.framework.restlet.teleapi.reader;

import colesico.framework.http.HttpContext;
import colesico.framework.http.HttpMethod;
import colesico.framework.restlet.teleapi.RestletJsonConverter;
import colesico.framework.restlet.teleapi.RestletTRContext;
import colesico.framework.router.RouterContext;
import colesico.framework.telehttp.Origin;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.io.InputStream;

import static colesico.framework.http.HttpMethod.*;

@Singleton
public final class JsonReader extends ObjectReader {

    protected final RestletJsonConverter jsonConverter;

    @Inject
    public JsonReader(Provider<RouterContext> routerContextProv, Provider<HttpContext> httpContextProv, RestletJsonConverter jsonConverter) {
        super(routerContextProv, httpContextProv);
        this.jsonConverter = jsonConverter;
    }

    @Override
    public Object read(RestletTRContext context) {

        HttpContext httpContext = httpContextProv.get();
        HttpMethod requestMethod = httpContext.getRequest().getRequestMethod();

        // Should the value be read from input stream?
        Origin origin = context.getOriginFacade().getOrigin();

        boolean useInputStream = origin.equals(Origin.BODY) ||
                (
                        origin.equals(Origin.AUTO) &&
                                (
                                        requestMethod.equals(HTTP_METHOD_POST)
                                                || requestMethod.equals(HTTP_METHOD_PUT)
                                                || requestMethod.equals(HTTP_METHOD_PATCH)
                                )
                );

        if (useInputStream) {
            try (InputStream is = httpContext.getRequest().getInputStream()) {
                return jsonConverter.fromJson(is, context.getValueType());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                String strValue = getStringValue(context);
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
