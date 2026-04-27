package colesico.framework.telehttp.codegen;

import colesico.framework.assist.codegen.model.ClassType;
import colesico.framework.service.codegen.model.teleapi.TWContextElement;
import colesico.framework.service.codegen.model.teleapi.TeleCommandElement;
import com.palantir.javapoet.CodeBlock;

public class HttpTWContextElement extends TWContextElement {

    protected final ClassType customWriter;

    public HttpTWContextElement(TeleCommandElement parentTeleCommand, CodeBlock creationCode, ClassType customWriter) {
        super(parentTeleCommand, creationCode);
        this.customWriter = customWriter;
    }

    public ClassType customWriter() {
        return customWriter;
    }
}
