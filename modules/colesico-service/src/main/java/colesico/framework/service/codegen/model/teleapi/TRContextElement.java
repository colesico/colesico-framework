package colesico.framework.service.codegen.model.teleapi;

import com.squareup.javapoet.CodeBlock;

/**
 * Represents tele data-reading context
 */
public class TRContextElement {

    /**
     * Parent tele-input ref
     */
    private final TeleInputElement parentInput;

    /**
     * Reading context creation code
     */
    private final CodeBlock creationCode;

    public TRContextElement(TeleInputElement parentInput, CodeBlock creationCode) {
        this.parentInput = parentInput;
        this.creationCode = creationCode;
    }

    public TeleInputElement getParentInput() {
        return parentInput;
    }

    public CodeBlock getCreationCode() {
        return creationCode;
    }
}
