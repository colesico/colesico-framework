package colesico.framework.service.codegen.model.teleapi;

import com.palantir.javapoet.CodeBlock;

/**
 * Represents tele-invocation context
 */
public class TIContextElement {

    /**
     * Parent tele-method ref
     */
    protected final TeleMethodElement parentTeleMethod;

    /**
     * Invocation context creation code
     */
    private final CodeBlock creationCode;

    public TIContextElement(TeleMethodElement parentTeleMethod, CodeBlock creationCode) {
        this.parentTeleMethod = parentTeleMethod;
        this.creationCode = creationCode;
    }

    public TeleMethodElement parentTeleMethod() {
        return parentTeleMethod;
    }

    public CodeBlock creationCode() {
        return creationCode;
    }
}
