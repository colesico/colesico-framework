package colesico.framework.restlet.assist;

/**
 * Single value holder.
 * Indent to  pass primitive items with rest json
 */
public class SingleValue<V> {

    private V value;

    public V value() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }
}
