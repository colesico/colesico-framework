package colesico.framework.restlet.codegen.assist;

import colesico.framework.restlet.teleapi.RestletOrigin;
import colesico.framework.restlet.teleapi.RestletParamReader;
import colesico.framework.service.codegen.model.teleapi.TeleEntryElement;
import colesico.framework.service.codegen.model.teleapi.TeleParameterElement;
import colesico.framework.telehttp.codegen.TeleHttpCodegenUtils;

import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;

public class RestletCodegenUtils {

    public static String getParamName(TeleEntryElement entry) {
        return TeleHttpCodegenUtils.getParamName(entry);
    }

    public static String getOriginName(TeleEntryElement teleArg) {
        return TeleHttpCodegenUtils.getOriginName(teleArg, RestletOrigin.AUTO);
    }

    public static TypeMirror getCustomReaderClass(TeleParameterElement teleParam, Elements elementUtils) {
        var rdAnn = teleParam.getOriginElement().getAnnotation(RestletParamReader.class);
        if (rdAnn == null) {
            rdAnn = teleParam.getParentTeleMethod().getServiceMethod().getOriginMethod().getAnnotation(RestletParamReader.class);
        }
        if (rdAnn == null) {
            return null;
        }
        return rdAnn.getValueTypeMirror(a -> a.value());
    }

}
