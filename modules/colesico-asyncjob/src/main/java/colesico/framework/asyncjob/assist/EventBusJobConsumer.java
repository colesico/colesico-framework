package colesico.framework.asyncjob.assist;

import colesico.framework.eventbus.EventBus;
import colesico.framework.asyncjob.JobConsumer;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Event bus based jobs consumer.
 * To use this consumer inject to queue config and return as consumer
 */
@Singleton
public final class EventBusJobConsumer implements JobConsumer<Object> {

    private final EventBus eventBus;

    @Inject
    public EventBusJobConsumer(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public void consume(Object jobPayload) {
        eventBus.send(jobPayload);
    }
}
