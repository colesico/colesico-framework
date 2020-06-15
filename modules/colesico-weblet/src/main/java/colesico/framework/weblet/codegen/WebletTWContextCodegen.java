package colesico.framework.weblet.codegen;

import colesico.framework.service.codegen.model.TeleMethodElement;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;

public class WebletTWContextCodegen {

    private final TeleMethodElement teleMethod;
    private final Class<?> contextClass;
    private final CodeBlock.Builder cb = CodeBlock.builder();

    public WebletTWContextCodegen(TeleMethodElement teleMethod, Class<?> contextClass) {
        this.teleMethod = teleMethod;
        this.contextClass = contextClass;
    }

    public CodeBlock generate() {
        CodeBlock.Builder cb = CodeBlock.builder();
        cb.add("new $T(", ClassName.get(contextClass));
        // TODO:
        cb.add("null");
        cb.add(")");
        return cb.build();
    }
}
