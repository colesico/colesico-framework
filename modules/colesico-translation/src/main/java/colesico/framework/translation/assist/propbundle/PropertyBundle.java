package colesico.framework.translation.assist.propbundle;

import java.util.*;

public class PropertyBundle {

    private final PropertyBundle parent;
    private final String baseName;
    private final Locale locale;
    private final Properties properties;

    public PropertyBundle(PropertyBundle parent, String baseName, Locale locale, Properties properties) {
        this.parent = parent;
        this.baseName = baseName;
        this.locale = locale;
        this.properties = properties;
    }

    public String getString(String key) {

        if (key == null) {
            throw new NullPointerException("Missing bundle key");
        }

        PropertyBundle p = this;

        do {
            String value = p.properties.getProperty(key);
            if (value != null) {
                return value;
            }
        } while ((p = p.parent) != null);

        return null;
    }

    public List<String> getKeys() {
        List<String> result = new ArrayList<>();
        Enumeration e = properties.keys();
        while (e.hasMoreElements()) {
            result.add(e.nextElement().toString());
        }
        return result;
    }

    public String getBaseName() {
        return baseName;
    }

    public Locale getLocale() {
        return locale;
    }
}
