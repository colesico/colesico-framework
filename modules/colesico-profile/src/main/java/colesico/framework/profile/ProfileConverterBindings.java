package colesico.framework.profile;

import colesico.framework.config.ConfigModel;
import colesico.framework.config.ConfigPrototype;

import java.util.Arrays;
import java.util.Iterator;

@ConfigPrototype(model = ConfigModel.POLYVARIANT)
abstract public class ProfileConverterBindings implements Iterable<ProfileConverterBindings.PropertyConverterBinding> {

    abstract public PropertyConverterBinding[] getConverterBindings();

    @Override
    final public Iterator<PropertyConverterBinding> iterator() {
        return Arrays.stream(getConverterBindings()).iterator();
    }

    public record PropertyConverterBinding<V>(Class<V> propertyClass, PropertyConverter<V> converter) {
    }

}
