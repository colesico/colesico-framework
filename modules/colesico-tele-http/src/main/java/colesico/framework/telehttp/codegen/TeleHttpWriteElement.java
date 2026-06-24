package colesico.framework.telehttp.codegen;

import colesico.framework.assist.codegen.model.ClassType;
import colesico.framework.service.codegen.model.teleapi.TeleWriteElement;
import colesico.framework.service.codegen.model.teleapi.TeleCommandElement;
import com.palantir.javapoet.CodeBlock;

public class TeleHttpWriteElement extends TeleWriteElement {

    protected final ClassType customWriter;

    public TeleHttpWriteElement(TeleCommandElement parentTeleCommand, CodeBlock resultTypeCode, CodeBlock optionsCode, ClassType customWriter) {
        super(parentTeleCommand, resultTypeCode, optionsCode);
        this.customWriter = customWriter;
    }

    public ClassType customWriter() {
        return customWriter;
    }
}
