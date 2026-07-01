package colesico.framework.restlet.reader;

import colesico.framework.assist.StringUtils;
import colesico.framework.http.HttpContext;
import colesico.framework.http.HttpMethod;
import colesico.framework.http.HttpRequest;
import colesico.framework.restlet.*;
import colesico.framework.telehttp.origin.OriginFactory;

import colesico.framework.telehttp.reader.OriginReader;
import colesico.framework.telehttp.writer.ValueSerializer;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;

import java.io.InputStream;

import static colesico.framework.http.HttpMethod.*;

@Singleton
public final class ObjectReader
        extends OriginReader<Object, RestletReadOptions>
        implements RestletTeleReader<Object> {

    private final ValueSerializer serializer;
    private final Provider<HttpContext> httpContextProv;

    @Inject
    public ObjectReader(OriginFactory originFactory, ValueSerializer serializer, Provider<HttpContext> httpContextProv) {
        super(originFactory);
        this.serializer = serializer;
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
                return serializer.deserialize(is, baseType);
            } catch (Exception e) {
                throw RestletException.of(e, 400);
            }
        } else {
            try {
                String strValue = readString(options.originName(), options.paramName());
                if (StringUtils.isBlank(strValue)) {
                    return null;
                }
                return serializer.deserialize(strValue, baseType);
            } catch (Exception e) {
                throw RestletException.of(e, 400);
            }
        }
    }

}
