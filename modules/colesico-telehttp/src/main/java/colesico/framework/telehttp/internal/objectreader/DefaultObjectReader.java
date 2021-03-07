package colesico.framework.telehttp.internal.objectreader;

import colesico.framework.http.HttpContext;
import colesico.framework.router.RouterContext;
import colesico.framework.telehttp.HttpTRContext;
import colesico.framework.telehttp.reader.ObjectReader;

import javax.inject.Provider;
import javax.inject.Singleton;

@Singleton
public class DefaultObjectReader<C extends HttpTRContext> extends ObjectReader<C> {

    protected final ReadingSchemeFactory readingSchemeFactory;

    public DefaultObjectReader(Provider<RouterContext> routerContextProv,
                               Provider<HttpContext> httpContextProv,
                               ReadingSchemeFactory readingSchemeFactory) {
        super(routerContextProv, httpContextProv);
        this.readingSchemeFactory = readingSchemeFactory;
    }

    @Override
    public Object read(C context) {
        ReadingScheme scheme = readingSchemeFactory.getScheme((Class) context.getValueType());
        return readObject(scheme, context);
    }

    private <T> T readObject(ReadingScheme<T> scheme, C context) {
        try {
            T object = scheme.getConstructor().newInstance();
            // Process value fields
            for (ReadingScheme.ValueField valueFiled : scheme.getValueFields()) {
                valueFiled.readValue(object, context.getName(), context.getOriginFacade());
            }
            // Process nested objects
            for (ReadingScheme.ObjectField objField : scheme.getObjectFields()) {
                objField.setObject(readObject(objField.getScheme(), context));
            }
            return object;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
