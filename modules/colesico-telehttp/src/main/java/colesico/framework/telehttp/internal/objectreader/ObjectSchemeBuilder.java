package colesico.framework.telehttp.internal.objectreader;

import colesico.framework.assist.StrUtils;
import colesico.framework.telehttp.HttpTRContext;
import colesico.framework.telehttp.HttpTeleReader;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ObjectSchemeBuilder {

    public <T> ReadingScheme<T> buildObjectScheme(Class<T> objectClass) {
        return null;
    }

    protected <T> ReadingScheme<T> buildObjectScheme(Class<T> objectClass, String namePrefix) {
        try {
            Constructor<T> constuctor = objectClass.getDeclaredConstructor();
            List<Method> setters = getSetters(objectClass);
            for (Method setter : setters) {
                // Http param name
                String paramName = toParamName(namePrefix, getFieldName(setter));
                Type valueType = setter.getParameterTypes()[0];
                T value = null;
                HttpTeleReader<T, HttpTRContext> reader = getReader(valueType);
                if (reader != null) {
                    value = reader.read(HttpTRContext.of(paramName, null));
                } else {
                    // value = buildObject(valueType, paramName);
                }
                //setter.invoke(compositionInstance, value);
            }
            //return compositionInstance;
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected <T> HttpTeleReader<T, HttpTRContext> getReader(Type valueType) {
        return null;
    }

    protected String toParamName(String prefix, String name) {
        return (prefix != null ? prefix + '.' : "") + name;
    }

    protected List<Method> getSetters(Class clazz) {
        List<Method> result = new ArrayList<>();
        Class<?> currentClazz = clazz;
        while (currentClazz.getSuperclass() != null) { // we don't want to process Object.class
            for (Method method : currentClazz.getDeclaredMethods()) {
                if (Modifier.isPublic(method.getModifiers())
                        && method.getParameterTypes().length == 1
                        && method.getReturnType() == void.class
                        && (method.getName().startsWith("set"))
                ) {
                    method.setAccessible(true);
                    result.add(method);
                }
            }
            currentClazz = currentClazz.getSuperclass();
        }
        return result;
    }

    protected String getFieldName(Method setter) {
        return StrUtils.firstCharToLowerCase(setter.getName().substring(3));
    }

}
