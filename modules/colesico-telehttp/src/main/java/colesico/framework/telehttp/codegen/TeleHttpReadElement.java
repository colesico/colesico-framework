package colesico.framework.telehttp.codegen;

import colesico.framework.assist.codegen.model.ClassType;
import colesico.framework.service.codegen.model.teleapi.TeleReadElement;
import colesico.framework.service.codegen.model.teleapi.TeleReadableElement;
import com.palantir.javapoet.CodeBlock;

public class TeleHttpReadElement extends TeleReadElement {

    protected final String paramName;

    protected final String originName;

    protected final ClassType customReader;

    public TeleHttpReadElement(TeleReadableElement parentReadable, CodeBlock valueTypeCode, CodeBlock optionsCode, String paramName, String originName, ClassType customReader) {
        super(parentReadable, valueTypeCode, optionsCode);
        this.paramName = paramName;
        this.originName = originName;
        this.customReader = customReader;
    }

    public String paramName() {
        return paramName;
    }

    public String originName() {
        return originName;
    }

    public ClassType customReader() {
        return customReader;
    }
}
