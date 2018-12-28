package colesico.framework.eventbus.codegen;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;

public class EventHandlerElement {

    private final ExecutableElement originMethod;
    private final TypeMirror eventType;

    public EventHandlerElement(ExecutableElement originMethod, TypeMirror eventType) {
        this.originMethod = originMethod;
        this.eventType = eventType;
    }

    public ExecutableElement getOriginMethod() {
        return originMethod;
    }

    public TypeMirror getEventType() {
        return eventType;
    }
}
