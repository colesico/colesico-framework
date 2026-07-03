package colesico.framework.restlet;

import colesico.framework.telehttp.HttpReadOptions;

import java.lang.reflect.Type;

/**
 *
 * @param paramName
 * @param originName
 * @param customReader Custom reader class or null. If null - default reader will be used to  read the parameter
 * @param metadata
 */
public record RestletReadOptions(
        Type baseType,
        String paramName,
        String originName,
        Class<? extends RestletTeleReader<?>> customReader,
        Object metadata
) implements HttpReadOptions {

    @Deprecated
    public RestletReadOptions(Type baseType, String paramName, String originName, Class<? extends RestletTeleReader<?>> customReader, Object metadata) {
        this.baseType = baseType;
        this.paramName = paramName;
        this.originName = originName;
        this.customReader = customReader;
        this.metadata = metadata;
    }

    public static Builder builder(Type baseType) {
        return new Builder(baseType);
    }

    public static class Builder {
        private final Type baseType;
        private String paramName;
        private String originName;
        private Class<? extends RestletTeleReader<?>> customReader;
        private Object metadata;

        public Builder(Type baseType) {
            this.baseType = baseType;
        }

        public Builder paramName(String paramName) {
            this.paramName = paramName;
            return this;
        }

        public Builder originName(String originName) {
            this.originName = originName;
            return this;
        }

        public Builder customReader(Class<? extends RestletTeleReader<?>> customReader) {
            this.customReader = customReader;
            return this;
        }

        public Builder metadata(Object metadata) {
            this.metadata = metadata;
            return this;
        }

        public RestletReadOptions build() {
            return new RestletReadOptions(baseType, paramName, originName, customReader, metadata);
        }
    }
}
