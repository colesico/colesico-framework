package colesico.framework.telehttp.codegen;

import colesico.framework.assist.codegen.model.ClassType;
import colesico.framework.service.codegen.model.teleapi.TROptionsElement;
import colesico.framework.service.codegen.model.teleapi.TeleOrdinaryParamElement;
import com.palantir.javapoet.CodeBlock;

public class HttpTROptionsElement extends TROptionsElement {

    protected final String paramName;

    protected final String originName;

    protected final ClassType customReader;

    public HttpTROptionsElement(TeleOrdinaryParamElement parentParameter, CodeBlock creationCode, String paramName, String originName, ClassType customReader) {
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
