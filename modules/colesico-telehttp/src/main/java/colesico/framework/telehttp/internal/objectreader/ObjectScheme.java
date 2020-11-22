package colesico.framework.telehttp.internal.objectreader;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public final class ObjectScheme<T> {

    private final Constructor<T> constructor;

    private final List<ValueField> valueFields = new ArrayList<>();

    private final List<ObjectScheme> objectFields = new ArrayList<>();

    public ObjectScheme(Constructor<T> constructor) {
        this.constructor = constructor;
    }

    public Constructor<T> getConstructor() {
        return constructor;
    }

    public List<ValueField> getValueFields() {
        return valueFields;
    }

    public List<ObjectScheme> getObjectFields() {
        return objectFields;
    }

    public static final class ValueField {
        private final String paramName;
        private final Method setter;

        public ValueField(String paramName, Method setter) {
            this.paramName = paramName;
            this.setter = setter;
        }

        public String getParamName() {
            return paramName;
        }

        public Method getSetter() {
            return setter;
        }
    }
}
