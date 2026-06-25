package colesico.framework.restlet.reader;

import colesico.framework.assist.ExceptionUtils;
import colesico.framework.assist.StringUtils;
import colesico.framework.http.HttpContext;
import colesico.framework.http.HttpMethod;
import colesico.framework.http.HttpRequest;
import colesico.framework.restlet.*;
import colesico.framework.telehttp.origin.OriginFactory;

import colesico.framework.telehttp.reader.OriginReader;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;

import java.io.InputStream;

import static colesico.framework.http.HttpMethod.*;

@Singleton
public final class JsonReader
        extends OriginReader<Object, RestletReadOptions>
        implements RestletTeleReader<Object> {

    private final RestletSerializer jsonConverter;
    private final Provider<HttpContext> httpContextProv;

    @Inject
    public JsonReader(OriginFactory originFactory, RestletSerializer jsonConverter, Provider<HttpContext> httpContextProv) {
        super(originFactory);
        this.jsonConverter = jsonConverter;
        this.httpContextProv = httpContextProv;
    }

    @Override
    public Object read(Class<Object> baseType, RestletReadOptions options) {
        HttpRequest request = httpContextProv.get().request();

        HttpMethod requestMethod = request.method();

        // Should the value be read from request input stream?
        String originName = options.originName();

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
                return jsonConverter.deserialize(is, baseType);
            } catch (Exception e) {
                throw new RestletException(new RestletError("ReadJsonError", ExceptionUtils.getRootCauseMessage(e), null));
            }
        } else {
            try {
                String strValue = readString(options.originName(), options.paramName());
                if (StringUtils.isBlank(strValue)) {
                    return null;
                }
                return jsonConverter.deserialize(strValue, baseType);
            } catch (Exception e) {
                throw new RestletException(new RestletError("ReadJsonError", ExceptionUtils.getRootCauseMessage(e), null));
            }
        }
    }

}
