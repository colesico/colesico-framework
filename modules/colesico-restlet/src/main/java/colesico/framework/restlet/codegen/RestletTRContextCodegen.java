package colesico.framework.restlet.codegen;

import colesico.framework.assist.codegen.model.AnnotationToolbox;
import colesico.framework.restlet.teleapi.RestletCustomReader;
import colesico.framework.service.codegen.model.TeleParamElement;
import colesico.framework.service.codegen.model.TeleVarElement;
import colesico.framework.weblet.codegen.WebletTRContextCodegen;
import colesico.framework.weblet.teleapi.OriginFacade;
import com.squareup.javapoet.TypeName;

import javax.lang.model.type.TypeMirror;

public class RestletTRContextCodegen extends WebletTRContextCodegen {
    public RestletTRContextCodegen(TeleParamElement teleParam, Class<?> contextClass, Class<? extends OriginFacade> originFacadeClass) {
        super(teleParam, contextClass, originFacadeClass);
    }

    @Override
    protected TypeName getCustomReaderClass(TeleVarElement rootVar) {
        AnnotationToolbox<RestletCustomReader> crAnn = teleParam.getOriginVariable().getAnnotation(RestletCustomReader.class);
        if (crAnn == null) {
            crAnn = rootVar.getOriginVariable().getAnnotation(RestletCustomReader.class);
        }
        if (crAnn == null) {
            return null;
        }
        TypeMirror readerClassMirror = crAnn.getValueTypeMirror(a -> a.value());
        return TypeName.get(readerClassMirror);
    }
}
