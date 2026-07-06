package colesico.framework.restlet;

import colesico.framework.telehttp.ContentType;
import colesico.framework.telehttp.HttpWriteOptions;

import java.lang.reflect.Type;

public record RestletWriteOptions(
        Type baseType,
        Integer statusCode,
        ContentType contentType,
        Class<? extends RestletWriter<?>> customWriter,
        Object metadata
) implements HttpWriteOptions {

    public static final String BUILDER_METHOD = "builder";
    public static final String BUILD_METHOD = "build";
    public static final String CUSTOM_WRITER_METHOD = "customWriter";

    @Deprecated
    public RestletWriteOptions(Type baseType, Integer statusCode, ContentType contentType, Class<? extends RestletWriter<?>> customWriter, Object metadata) {
        this.baseType = baseType;
        this.statusCode = statusCode;
        this.contentType = contentType;
        this.customWriter = customWriter;
        this.metadata = metadata;
    }

    public static Builder builder(Type baseType) {
        return new Builder(baseType);
    }

    public static class Builder {
        private final Type baseType;
        private Integer statusCode;
        private ContentType contentType;
        private Class<? extends RestletWriter<?>> customWriter;
        private Object metadata;

        public Builder(Type baseType) {
            this.baseType = baseType;
        }

        public Builder statusCode(Integer statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public Builder contentType(ContentType contentType) {
            this.contentType = contentType;
            return this;
        }

        public Builder customWriter(Class<? extends RestletWriter<?>> customWriter) {
            this.customWriter = customWriter;
            return this;
        }

        public Builder metadata(Object metadata) {
            this.metadata = metadata;
            return this;
        }

        public RestletWriteOptions build() {
            return new RestletWriteOptions(baseType, statusCode, contentType, customWriter, metadata);
        }
    }
}
