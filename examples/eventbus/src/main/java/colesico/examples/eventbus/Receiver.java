package colesico.examples.eventbus;

import colesico.framework.eventbus.OnEvent;
import colesico.framework.service.Service;

@Service
public class Receiver {

    @OnEvent
    public void onEvent1(MyEvent1 event) {
        System.out.println("Receiver on MyEvent1: " + event.message);
    }

    @OnEvent
    public void onEvent2(MyEvent2 event) {
        System.out.println("Receiver on MyEvent2: " + event.message);
    }
}
