package colesico.examples.eventbus;

import colesico.framework.eventbus.EventBus;
import colesico.framework.eventbus.OnEvent;
import colesico.framework.service.Service;

@Service
public class Sender {

    final EventBus eventBus;

    public Sender(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void sendEvent() {
        eventBus.send(new MyEvent1("hello 1"));
        eventBus.send(new MyEvent2("hello 2"));
    }

    @OnEvent
    public void onEvent2(MyEvent2 event) {
        System.out.println("Sender on MyEvent2: " + event.message);
    }
}
