package colesico.framework.telehttp.internal.objectreader;

import colesico.framework.telehttp.HttpTRContext;
import colesico.framework.telehttp.OriginFactory;
import colesico.framework.telehttp.reader.ObjectReader;

import javax.inject.Singleton;

@Singleton
public class DefaultObjectReader<C extends HttpTRContext> extends ObjectReader<C> {

    protected final ReadingSchemeFactory readingSchemeFactory;

    public DefaultObjectReader(OriginFactory originFactory, ReadingSchemeFactory readingSchemeFactory) {
        super(originFactory);
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
                valueFiled.readValue(object, context.getName(), context.getOriginName());
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
