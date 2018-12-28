package colesico.framework.eventbus;

import javax.inject.Provider;

abstract public class EventsListener<S> {
    public static String GET_BINDINGS_METHOD = "getBindings";
    public static String TARGET_PROV_FIELD = "targetProv";

    protected final Provider<S> targetProv;

    public EventsListener(Provider<S> targetProv) {
        this.targetProv = targetProv;
    }

    abstract public EventBinding[] getBindings();
}
