package colesico.framework.service.codegen.model.teleapi;

import com.palantir.javapoet.CodeBlock;

/**
 * Represents tele-reading options
 */
public class ReadOptionsElement {

    /**
     * Parent tele-readable ref
     */
    private final TeleReadableElement parentReadable;

    /**
     * Reading context creation code
     */
    private final CodeBlock creationCode;

    public ReadOptionsElement(TeleReadableElement parentReadable, CodeBlock creationCode) {
        this.parentReadable = parentReadable;
        this.creationCode = creationCode;
    }

    public TeleReadableElement parentReadable() {
        return parentReadable;
    }

    public CodeBlock creationCode() {
        return creationCode;
    }
}
