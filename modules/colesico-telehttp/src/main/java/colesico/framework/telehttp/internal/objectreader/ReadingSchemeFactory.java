package colesico.framework.telehttp.internal.objectreader;

import colesico.framework.assist.StrUtils;
import colesico.framework.ioc.Ioc;
import colesico.framework.ioc.key.ClassedKey;
import colesico.framework.telehttp.HttpTRContext;
import colesico.framework.telehttp.HttpTeleReader;
import colesico.framework.telehttp.ParamName;

import javax.inject.Singleton;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public class ReadingSchemeFactory {

    private final Ioc ioc;

    private final Map<Class<?>, ReadingScheme<?>> cache = new ConcurrentHashMap<>();

    public ReadingSchemeFactory(Ioc ioc) {
        this.ioc = ioc;
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
            List<Field> fields = getAllFields(objectClass);
            for (Field field : fields) {
                // Http param name
                String paramName = getParamName(namePrefix, field);
                Type fieldType = field.getGenericType();
                Method setter = getSetter(objectClass, fieldType, field.getName());
                HttpTeleReader<T, HttpTRContext> reader = getReader(fieldType);

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

    protected <T> HttpTeleReader<T, HttpTRContext> getReader(Type valueType) {
        HttpTeleReader reader = ioc.instance(new ClassedKey<>(HttpTeleReader.class.getCanonicalName(), typeToClassName(valueType)), null);
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
    protected List<Field> getAllFields(Class clazz) {
        List<Field> result = new ArrayList<>();
        Class<?> currentClass = clazz;
        while (currentClass.getSuperclass() != null) {
            for (Field field : currentClass.getDeclaredFields()) {
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
        return null;
    }


    protected String typeToClassName(Type valueType) {
        if (valueType instanceof Class) {
            return ((Class) valueType).getCanonicalName();
        } else {
            return valueType.getTypeName();
        }
    }
}
