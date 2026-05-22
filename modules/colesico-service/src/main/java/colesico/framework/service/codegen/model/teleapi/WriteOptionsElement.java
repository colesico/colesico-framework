package colesico.framework.service.codegen.model.teleapi;

import com.palantir.javapoet.CodeBlock;

/**
 * @see colesico.framework.teleapi.dataport.WriteOptions
 */
public class WriteOptionsElement {

    /**
     * Parent tele-command ref
     */
    protected final TeleCommandElement parentTeleCommand;

    /**
     * Write options creation code
     */
    private final CodeBlock creationCode;

    public WriteOptionsElement(TeleCommandElement parentTeleCommand, CodeBlock creationCode) {
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
