package colesico.framework.service.codegen.model.teleapi;

import com.palantir.javapoet.CodeBlock;

public class TWContextElement {

    /**
     * Parent tele-command ref
     */
    protected final TeleCommandElement parentTeleCommand;

    /**
     * Method result writing context creation code
     */
    private final CodeBlock creationCode;

    public TWContextElement(TeleCommandElement parentTeleCommand, CodeBlock creationCode) {
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
