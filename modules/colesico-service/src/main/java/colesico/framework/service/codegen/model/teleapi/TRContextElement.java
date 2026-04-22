package colesico.framework.service.codegen.model.teleapi;

import com.palantir.javapoet.CodeBlock;

/**
 * Represents tele-reading context
 */
public class TRContextElement {

    /**
     * Parent tele-readable ref
     */
    private final TeleReadableElement parentReadable;

    /**
     * Reading context creation code
     */
    private final CodeBlock creationCode;

    public TRContextElement(TeleParameterElement parentReadable, CodeBlock creationCode) {
        this.parentReadable = parentReadable;
        this.creationCode = creationCode;
    }

    public TeleParameterElement getParentReadable() {
        return parentReadable;
    }

    public CodeBlock getCreationCode() {
        return creationCode;
    }
}
