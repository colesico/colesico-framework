package colesico.framework.eventbus.internal;

import colesico.framework.eventbus.EventBus;
import colesico.framework.ioc.Produce;
import colesico.framework.ioc.Producer;

import javax.inject.Singleton;

@Producer
@Produce(EventBusImpl.class)
public class EventBusProducer {

    @Singleton
    public EventBus getEventBus(EventBusImpl impl) {
        return impl;
    }
}
