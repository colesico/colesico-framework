package colesico.framework.service.codegen.model;

import com.squareup.javapoet.CodeBlock;

/**
 * Represents tele data-reading context
 */
public class TRContextElement {

    /**
     * Parent tele-param ref
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
