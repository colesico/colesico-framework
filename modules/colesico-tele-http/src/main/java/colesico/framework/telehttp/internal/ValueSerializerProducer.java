package colesico.framework.telehttp.internal;

import colesico.framework.ioc.Ioc;
import colesico.framework.ioc.key.NamedKey;
import colesico.framework.ioc.message.IocMessage;
import colesico.framework.ioc.production.Produce;
import colesico.framework.ioc.production.Producer;
import colesico.framework.ioc.scope.Unscoped;
import colesico.framework.telehttp.writer.ValueSerializer;
import colesico.framework.telehttp.writer.ToStringSerializer;
import jakarta.inject.Named;

@Producer
@Produce(value = ToStringSerializer.class)
public class ValueSerializerProducer {

    /**
     * ValueSerializer factory
     */
    @Unscoped
    public ValueSerializer valueSerializer(@IocMessage String mimeType, Ioc ioc) {
        var key = new NamedKey<>(ValueSerializer.class, mimeType);
        return ioc.instance(key);
    }

    @Named("text/plain")
    public ValueSerializer textPlainSerializer(ToStringSerializer impl) {
        return impl;
    }

    @Named("text/html")
    public ValueSerializer textHtmlSerializer(ToStringSerializer impl) {
        return impl;
    }
}
