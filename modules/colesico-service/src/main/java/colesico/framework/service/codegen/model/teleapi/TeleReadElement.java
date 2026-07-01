package colesico.framework.service.codegen.model.teleapi;

import com.palantir.javapoet.CodeBlock;

/**
 * Data-port read value specification
 */
public class TeleReadElement {

    /**
     * Parent tele-readable ref
     */
    private final TeleReadableElement parentReadable;

    /**
     * Value type code
     */
    private final CodeBlock valueTypeCode;

    /**
     * Read options creation code
     */
    private final CodeBlock optionsCode;

    public TeleReadElement(TeleReadableElement parentReadable, CodeBlock valueTypeCode, CodeBlock optionsCode) {
        this.parentReadable = parentReadable;
        this.valueTypeCode = valueTypeCode;
        this.optionsCode = optionsCode;
    }

    public TeleReadableElement parentReadable() {
        return parentReadable;
    }

    public CodeBlock optionsCode() {
        return optionsCode;
    }

    public CodeBlock valueTypeCode() {
        return valueTypeCode;
    }
}
