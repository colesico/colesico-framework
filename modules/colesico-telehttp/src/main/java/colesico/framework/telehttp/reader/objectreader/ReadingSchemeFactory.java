package colesico.framework.telehttp.reader.objectreader;

import colesico.framework.assist.StrUtils;
import colesico.framework.teleapi.TeleFactory;
import colesico.framework.telehttp.HttpTRContext;
import colesico.framework.telehttp.HttpTeleReader;
import colesico.framework.telehttp.LocalField;
import colesico.framework.telehttp.ParamName;

import javax.inject.Singleton;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public class ReadingSchemeFactory {

    private final TeleFactory teleFactory;
    private final Map<Class<?>, ReadingScheme<?>> cache = new ConcurrentHashMap<>();

    public ReadingSchemeFactory(TeleFactory teleFactory) {
        this.teleFactory = teleFactory;
    }

    protected Class<? extends HttpTeleReader> getReaderType() {
        return HttpTeleReader.class;
    }

    public <T> ReadingScheme<T> getScheme(Class<T> objectClass) {
        return (ReadingScheme<T>) cache.computeIfAbsent(objectClass, k -> {
            ReadingScheme<T> scheme = buildScheme(objectClass, "");
            return scheme;
        });
    }

    protected <T> ReadingScheme<T> buildScheme(Class<T> objectClass, String namePrefix) {
        try {
            Constructor<T> constructor = objectClass.getDeclaredConstructor();
            ReadingScheme<T> scheme = new ReadingScheme<>(constructor);
            List<Field> fields = getFields(objectClass);
            for (Field field : fields) {

                // Http param name
                String paramName = getParamName(namePrefix, field);
                Type fieldType = field.getGenericType();
                Method setter = getSetter(objectClass, fieldType, field.getName());
                HttpTeleReader<T, HttpTRContext> reader = findReader(fieldType);

                if (reader != null) {
                    ReadingScheme.ValueField valueField = new ReadingScheme.ValueField(paramName, setter, reader);
                    scheme.addValueField(valueField);
                } else {
                    ReadingScheme fieldScheme = buildScheme((Class<T>) fieldType, namePrefix);
                    ReadingScheme.ObjectField objectField = new ReadingScheme.ObjectField(setter, fieldScheme);
                    scheme.addObjectField(objectField);
                }
            }
            return scheme;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected HttpTeleReader findReader(Type valueType) {
        HttpTeleReader reader = teleFactory.findReader(getReaderType(), valueType);
        return reader;
    }

    protected String getParamName(String namePrefix, Field field) {
        ParamName pn = field.getAnnotation(ParamName.class);
        if (pn != null) {
            return namePrefix + pn.value();
        }
        return namePrefix + field.getName();
    }

    // Fields with inheritance
    protected List<Field> getFields(Class clazz) {
        List<Field> result = new ArrayList<>();
        Class<?> currentClass = clazz;
        while (currentClass.getSuperclass() != null) {
            for (Field field : currentClass.getDeclaredFields()) {
                LocalField lf = field.getAnnotation(LocalField.class);
                if (lf != null) {
                    continue;
                }
                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }
                result.add(field);
            }
            currentClass = currentClass.getSuperclass();
        }
        return result;
    }

    protected Method getSetter(Class<?> clazz, Type fieldType, String fieldName) {
        String setterName = "set" + StrUtils.firstCharToUpperCase(fieldName);
        Class<?> currentClass = clazz;
        while (currentClass.getSuperclass() != null) {
            for (Method method : currentClass.getDeclaredMethods()) {
                if (Modifier.isPublic(method.getModifiers())
                        && method.getGenericParameterTypes().length == 1
                        && method.getGenericParameterTypes()[0].equals(fieldType)
                        && method.getReturnType() == void.class
                        && (method.getName().equals(setterName))
                ) {
                    return method;
                }
            }
            currentClass = currentClass.getSuperclass();
        }
        throw new RuntimeException("Setter not found for field: " + clazz.getCanonicalName() + "." + fieldName);
    }

}
