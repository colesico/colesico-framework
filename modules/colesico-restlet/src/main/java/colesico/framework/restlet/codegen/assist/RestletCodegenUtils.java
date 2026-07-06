package colesico.framework.restlet.codegen.assist;

import colesico.framework.service.codegen.model.teleapi.TeleOrdinaryParamElement;
import colesico.framework.service.codegen.model.teleapi.TeleParameterElement;
import colesico.framework.telehttp.codegen.TeleHttpCodegenUtils;
import colesico.framework.telehttp.origin.Origin;

import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;

public class RestletCodegenUtils {

    public static TypeMirror customReaderClass(TeleOrdinaryParamElement teleParam, Elements elementUtils) {
        var rdAnn = teleParam.originElement().annotation(RestletCustomReader.class);
        if (rdAnn == null) {
            rdAnn = teleParam.parentTeleCommand().serviceMethod().originMethod().annotation(RestletCustomReader.class);
        }
        if (rdAnn == null) {
            return null;
        }
        return rdAnn.valueTypeMirror(a -> a.value());
    }

}
