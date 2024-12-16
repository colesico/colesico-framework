package colesico.framework.profile;

import colesico.framework.config.ConfigModel;
import colesico.framework.config.ConfigPrototype;

import java.util.Arrays;
import java.util.Iterator;

@ConfigPrototype(model = ConfigModel.POLYVARIANT)
abstract public class ProfileConfigPrototype implements Iterable<ProfileConfigPrototype.ValueConverterBinding> {

    abstract public ValueConverterBinding[] getConverterBindings();

    @Override
    final public Iterator<ValueConverterBinding> iterator() {
        return Arrays.stream(getConverterBindings()).iterator();
    }

    public record ValueConverterBinding<V>(Class<V> valueClass, PropertyConverter<V> converter) {
    }


}
