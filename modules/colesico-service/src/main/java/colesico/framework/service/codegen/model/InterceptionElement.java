package colesico.framework.service.codegen.model;

import com.squareup.javapoet.CodeBlock;

public class InterceptionElement {
    private final CodeBlock interceptor;
    private final CodeBlock parameters;

    public InterceptionElement(CodeBlock interceptor, CodeBlock parameters) {
        this.interceptor = interceptor;
        this.parameters = parameters;
    }

    public InterceptionElement(CodeBlock interceptor) {
        this.interceptor = interceptor;
        this.parameters = null;
    }

    public CodeBlock getInterceptor() {
        return interceptor;
    }

    public CodeBlock getParameters() {
        return parameters;
    }
}
