package colesico.framework.translation.internal;

import colesico.framework.ioc.Produce;
import colesico.framework.ioc.Producer;
import colesico.framework.ioc.Rank;
import colesico.framework.translation.TranslationKit;

import javax.inject.Singleton;

@Producer(Rank.RANK_MINOR)
@Produce(TranslationKitImpl.class)
public class TranslationProducer {

    @Singleton
    public TranslationKit getTranslationKit(TranslationKitImpl impl) {
        return impl;
    }

}
