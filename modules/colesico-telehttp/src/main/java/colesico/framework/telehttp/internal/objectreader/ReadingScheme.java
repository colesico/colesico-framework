package colesico.framework.telehttp.internal.objectreader;

import colesico.framework.telehttp.HttpTRContext;
import colesico.framework.telehttp.HttpTeleReader;
import colesico.framework.telehttp.Origin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Object reading scheme
 */
public final class ReadingScheme<T> {

    /**
     * Target class constructor
     */
    private final Constructor<T> constructor;

    /**
     * The fields that values can be read from remote client
     */
    private final List<ValueField> valueFields = new ArrayList<>();

    /**
     * Fields that represents nested objects
     */
    private final List<ObjectField> objectFields = new ArrayList<>();

    public ReadingScheme(Constructor<T> cr) {
        this.constructor = cr;
    }

    public void addValueField(ValueField filed) {
        valueFields.add(filed);
    }

    public void addObjectField(ObjectField field) {
        objectFields.add(field);
    }

    public Constructor<T> getConstructor() {
        return constructor;
    }

    public List<ValueField> getValueFields() {
        return valueFields;
    }

    public List<ObjectField> getObjectFields() {
        return objectFields;
    }

    public static final class ObjectField {

        /**
         * Object reading scheme
         */
        private final ReadingScheme scheme;

        /**
         * Object field setter for target object
         */
        private final Method setter;

        public ObjectField(Method setter, ReadingScheme scheme) {
            this.setter = setter;
            this.scheme = scheme;
        }

        /**
         * Set object field value
         */
        public void setObject(Object object) throws Exception {
            setter.invoke(object);
        }

        public ReadingScheme getScheme() {
            return scheme;
        }
    }

    public static final class ValueField {

        /**
         * Http param name base part
         */
        private final String paramName;

        /**
         * Value setter for target object
         */
        private final Method setter;

        /**
         * Value reader
         */
        private final HttpTeleReader reader;

        public ValueField(String paramName, Method setter, HttpTeleReader reader) {
            this.paramName = paramName;
            this.setter = setter;
            this.reader = reader;
        }

        /**
         * Reads tele-value and set the field value
         */
        public void readValue(Object target, String namePrefix, String originName) throws Exception {
            Object value = reader.read(HttpTRContext.of(namePrefix + paramName, originName));
            setter.invoke(target, value);
        }
    }
}
