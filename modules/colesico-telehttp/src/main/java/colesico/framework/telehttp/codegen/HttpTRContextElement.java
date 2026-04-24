package colesico.framework.telehttp.codegen;

import colesico.framework.assist.codegen.model.ClassType;
import colesico.framework.service.codegen.model.teleapi.TRContextElement;
import colesico.framework.service.codegen.model.teleapi.TeleParameterElement;
import com.palantir.javapoet.CodeBlock;

public class HttpTRContextElement extends TRContextElement {

    protected final String paramName;

    protected final String originName;

    protected final ClassType customReader;

    public HttpTRContextElement(TeleParameterElement parentParameter, CodeBlock creationCode, String paramName, String originName, ClassType customReader) {
        super(parentParameter, creationCode);
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
