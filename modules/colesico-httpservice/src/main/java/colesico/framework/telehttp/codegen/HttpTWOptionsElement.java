package colesico.framework.telehttp.codegen;

import colesico.framework.assist.codegen.model.ClassType;
import colesico.framework.service.codegen.model.teleapi.TWOptionsElement;
import colesico.framework.service.codegen.model.teleapi.TeleCommandElement;
import com.palantir.javapoet.CodeBlock;

public class HttpTWOptionsElement extends TWOptionsElement {

    protected final ClassType customWriter;

    public HttpTWOptionsElement(TeleCommandElement parentTeleCommand, CodeBlock creationCode, ClassType customWriter) {
        super(parentTeleCommand, creationCode);
        this.customWriter = customWriter;
    }

    public ClassType customWriter() {
        return customWriter;
    }
}
