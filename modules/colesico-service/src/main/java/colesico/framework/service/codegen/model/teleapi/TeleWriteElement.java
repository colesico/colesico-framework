package colesico.framework.service.codegen.model.teleapi;

import com.palantir.javapoet.CodeBlock;

/**
 * Data-port write result specification
 */
public class TeleWriteElement {

    /**
     * Parent tele-command ref
     */
    protected final TeleCommandElement parentTeleCommand;

    /**
     * Code for obtaining the base type of the value written to the data port
     */
    private final CodeBlock valueTypeCode;

    /**
     * Write options creation code
     *
     * @see colesico.framework.teleapi.dataport.WriteOptions
     */
    private final CodeBlock optionsCode;

    public TeleWriteElement(TeleCommandElement parentTeleCommand, CodeBlock valueTypeCode, CodeBlock optionsCode) {
        this.parentTeleCommand = parentTeleCommand;
        this.valueTypeCode = valueTypeCode;
        this.optionsCode = optionsCode;
    }

    public TeleCommandElement parentTeleCommand() {
        return parentTeleCommand;
    }

    public CodeBlock optionsCode() {
        return optionsCode;
    }

    public CodeBlock valueTypeCode() {
        return valueTypeCode;
    }
}
