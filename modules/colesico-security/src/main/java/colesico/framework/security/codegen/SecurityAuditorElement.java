package colesico.framework.security.codegen;

import colesico.framework.assist.codegen.model.ClassElement;

public class SecurityAuditorElement {
    private final ClassElement auditorClass;


    public SecurityAuditorElement(ClassElement auditorClass) {
        this.auditorClass = auditorClass;
    }

    public ClassElement getAuditorClass() {
        return auditorClass;
    }
}
