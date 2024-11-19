package colesico.framework.telehttp.codegen;

import colesico.framework.assist.codegen.model.ClassType;
import colesico.framework.service.codegen.model.teleapi.TWContextElement;
import colesico.framework.service.codegen.model.teleapi.TeleMethodElement;
import com.palantir.javapoet.CodeBlock;

public class HttpTWContextElement extends TWContextElement {

    protected final ClassType customWriter;

    public HttpTWContextElement(TeleMethodElement parentTeleMethod, CodeBlock creationCode, ClassType customWriter) {
        super(parentTeleMethod, creationCode);
        this.customWriter = customWriter;
    }

    public ClassType getCustomWriter() {
        return customWriter;
    }
}
