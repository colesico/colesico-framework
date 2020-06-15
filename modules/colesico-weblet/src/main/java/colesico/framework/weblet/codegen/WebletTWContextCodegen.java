package colesico.framework.weblet.codegen;

import colesico.framework.assist.codegen.model.AnnotationToolbox;
import colesico.framework.service.codegen.model.TeleMethodElement;
import colesico.framework.weblet.teleapi.WebletCustomWriter;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.TypeName;

import javax.lang.model.type.TypeMirror;

public class WebletTWContextCodegen {

    protected final TeleMethodElement teleMethod;
    protected final Class<?> contextClass;
    protected final CodeBlock.Builder cb = CodeBlock.builder();

    public WebletTWContextCodegen(TeleMethodElement teleMethod, Class<?> contextClass) {
        this.teleMethod = teleMethod;
        this.contextClass = contextClass;
    }

    protected TypeName getCustomWriterClass() {
        AnnotationToolbox<WebletCustomWriter> cwAnn = teleMethod.getProxyMethod().getOriginMethod().getAnnotation(WebletCustomWriter.class);
        if (cwAnn == null) {
            cwAnn = teleMethod.getParentTeleFacade().getParentService().getOriginClass().getAnnotation(WebletCustomWriter.class);
        }
        if (cwAnn == null) {
            return null;
        }
        TypeMirror readerClassMirror = cwAnn.getValueTypeMirror(a -> a.value());
        return TypeName.get(readerClassMirror);
    }

    public CodeBlock generate() {
        cb.add("new $T(", ClassName.get(contextClass));

        TypeName customWriter = getCustomWriterClass();
        if (customWriter == null) {
            cb.add("null");
        } else {
            cb.add("$T.class", customWriter);
        }
        cb.add(")");
        return cb.build();
    }
}
