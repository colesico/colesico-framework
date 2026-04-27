package colesico.framework.service.codegen.model.teleapi;

import com.palantir.javapoet.CodeBlock;

/**
 * Represents tele-invocation context
 */
public class TIContextElement {

    /**
     * Parent tele-command ref
     */
    protected final TeleCommandElement parentTeleCommand;

    /**
     * Invocation context creation code
     */
    private final CodeBlock creationCode;

    public TIContextElement(TeleCommandElement parentTeleCommand, CodeBlock creationCode) {
        this.parentTeleCommand = parentTeleCommand;
        this.creationCode = creationCode;
    }

    public TeleCommandElement parentTeleCommand() {
        return parentTeleCommand;
    }

    public CodeBlock creationCode() {
        return creationCode;
    }
}
