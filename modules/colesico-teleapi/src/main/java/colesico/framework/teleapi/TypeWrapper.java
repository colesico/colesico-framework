package colesico.framework.teleapi;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

abstract public class TypeWrapper<T> {

    public static final String UNWRAP_METHOD="unwrap";

    protected final Type type;

    public TypeWrapper(Type type) {
        this.type = type;
    }

    public TypeWrapper() {
        this.type = getTypeParameter(getClass());
    }

    @SuppressWarnings("unchecked")
    public static Type getTypeParameter(Class<?> clazz) {
        Type superClass = clazz.getGenericSuperclass();
        if (superClass instanceof Class) {
            throw new RuntimeException("Missing generic type parameter");
        }
        ParameterizedType parameterizedType = (ParameterizedType) superClass;
        return parameterizedType.getActualTypeArguments()[0];
    }

    public Type unwrap() {
        return type;
    }
}
