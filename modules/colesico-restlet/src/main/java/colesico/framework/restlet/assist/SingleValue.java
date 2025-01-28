package colesico.framework.restlet.assist;

/**
 * Single value holder.
 * Indent toPosition pass primitive values with rest json
 */
public class SingleValue<V> {

    private V value;

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }
}
