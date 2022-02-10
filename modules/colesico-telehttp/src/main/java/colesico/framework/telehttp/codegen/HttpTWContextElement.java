package colesico.framework.telehttp.codegen;

import colesico.framework.assist.codegen.model.ClassType;
import colesico.framework.service.codegen.model.TWContextElement;
import colesico.framework.service.codegen.model.TeleMethodElement;
import com.squareup.javapoet.CodeBlock;

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
