package colesico.framework.eventbus.codegen;

import colesico.framework.assist.codegen.model.ClassType;
import colesico.framework.assist.codegen.model.MethodElement;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;

public class EventHandlerElement {

    private final MethodElement originMethod;
    private final ClassType eventType;

    public EventHandlerElement(MethodElement originMethod, ClassType eventType) {
        this.originMethod = originMethod;
        this.eventType = eventType;
    }

    public MethodElement getOriginMethod() {
        return originMethod;
    }

    public ClassType getEventType() {
        return eventType;
    }
}
