package colesico.framework.translation.internal.propertybundle;

import java.util.*;

public class PropertyBundle {

    private final PropertyBundle parent;
    private final String name;
    private final Properties properties;

    public PropertyBundle(PropertyBundle parent, String name, Properties properties) {
        this.parent = parent;
        this.name = name;
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

    public Collection<String> getKeys() {
        Collection<String> result = new ArrayList<>();
        Enumeration e = properties.keys();
        while (e.hasMoreElements()) {
            result.add(e.nextElement().toString());
        }
        return result;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "PropertyBundle{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PropertyBundle that = (PropertyBundle) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
