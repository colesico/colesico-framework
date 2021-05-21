package colesico.framework.telehttp;

import colesico.framework.ioc.Ioc;
import colesico.framework.ioc.key.NamedKey;

import javax.inject.Singleton;

@Singleton
public final class OriginFactory {

    private final Ioc ioc;

    public OriginFactory(Ioc ioc) {
        this.ioc = ioc;
    }

    public Origin getOrigin(String originName) {
        return ioc.instance(new NamedKey<>(Origin.class, originName), null);
    }
}
