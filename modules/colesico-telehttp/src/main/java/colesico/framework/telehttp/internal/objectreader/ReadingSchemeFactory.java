package colesico.framework.telehttp.internal.objectreader;

import javax.inject.Singleton;

@Singleton
public class ReadingSchemeFactory {
    public <T> ReadingScheme<T> getObjectScheme(Class<T> objectClass) {
        return null;
    }
}
