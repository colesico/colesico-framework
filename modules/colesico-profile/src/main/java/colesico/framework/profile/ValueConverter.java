package colesico.framework.profile;

public interface ValueConverter<V> {
    /**
     * To property name,value
     */
    String[] toProperty(V value);

    V fromProperty(String name, String value);

    byte[] serialize(V value);

    byte[] deserialize(V value);
}
