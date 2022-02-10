package colesico.framework.service.codegen.model;

import com.squareup.javapoet.CodeBlock;

public class TWContextElement {

    /**
     * Parent tele-method ref
     */
    protected final TeleMethodElement parentTeleMethod;

    /**
     * Method result writing context creation code
     */
    private final CodeBlock creationCode;

    public TWContextElement(TeleMethodElement parentTeleMethod, CodeBlock creationCode) {
        this.parentTeleMethod = parentTeleMethod;
        this.creationCode = creationCode;
    }

    public TeleMethodElement getParentTeleMethod() {
        return parentTeleMethod;
    }

    public CodeBlock getCreationCode() {
        return creationCode;
    }
}
