package colesico.framework.restlet.codegen;

import colesico.framework.assist.codegen.model.AnnotationToolbox;
import colesico.framework.restlet.teleapi.RestletCustomWriter;
import colesico.framework.service.codegen.model.TeleMethodElement;
import colesico.framework.weblet.codegen.WebletTWContextCodegen;
import colesico.framework.weblet.teleapi.WebletCustomWriter;
import com.squareup.javapoet.TypeName;

import javax.lang.model.type.TypeMirror;

public class RestletTWContextCodegen extends WebletTWContextCodegen {
    public RestletTWContextCodegen(TeleMethodElement teleMethod, Class<?> contextClass) {
        super(teleMethod, contextClass);
    }

    @Override
    protected TypeName getCustomWriterClass() {
        AnnotationToolbox<RestletCustomWriter> cwAnn = teleMethod.getProxyMethod().getOriginMethod().getAnnotation(RestletCustomWriter.class);
        if (cwAnn == null) {
            cwAnn = teleMethod.getParentTeleFacade().getParentService().getOriginClass().getAnnotation(RestletCustomWriter.class);
        }
        if (cwAnn == null) {
            return null;
        }
        TypeMirror readerClassMirror = cwAnn.getValueTypeMirror(a -> a.value());
        return TypeName.get(readerClassMirror);
    }
}
