package colesico.framework.service.codegen.model.teleapi;

import com.palantir.javapoet.CodeBlock;

/**
 * Represents tele-reading context
 */
public class TRContextElement {

    /**
     * Parent tele-input ref
     */
    private final TeleParameterElement parentParameter;

    /**
     * Reading context creation code
     */
    private final CodeBlock creationCode;

    public TRContextElement(TeleParameterElement parentParameter, CodeBlock creationCode) {
        this.parentParameter = parentParameter;
        this.creationCode = creationCode;
    }

    public TeleParameterElement getParentParameter() {
        return parentParameter;
    }

    public CodeBlock getCreationCode() {
        return creationCode;
    }
}
