package colesico.framework.restlet.codegen.assist;

import colesico.framework.assist.codegen.CodegenUtils;
import colesico.framework.restlet.codegen.model.JsonFieldElement;
import colesico.framework.restlet.teleapi.RestletOrigin;
import colesico.framework.restlet.teleapi.RestletParamReader;
import colesico.framework.restlet.teleapi.reader.JsonFieldReader;
import colesico.framework.service.codegen.model.teleapi.TeleEntryElement;
import colesico.framework.service.codegen.model.teleapi.TeleParameterElement;
import colesico.framework.telehttp.codegen.TeleHttpCodegenUtils;
import org.apache.commons.lang3.StringUtils;

import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;

public class RestletCodegenUtils {

    public static String getParamName(TeleEntryElement entry) {
        String paramName = TeleHttpCodegenUtils.getParamName(entry);
        JsonFieldElement jfe = entry.getProperty(JsonFieldElement.class);
        if (jfe != null) {
            if (StringUtils.isNotBlank(jfe.getName())) {
                paramName = jfe.getName();
            }
        }
        return paramName;
    }

    public static String getOriginName(TeleEntryElement teleArg) {
        String originName;
        JsonFieldElement jfe = teleArg.getProperty(JsonFieldElement.class);
        if (jfe != null) {
            originName = RestletOrigin.JSON_FIELD;
        } else {
            originName = TeleHttpCodegenUtils.getOriginName(teleArg, RestletOrigin.AUTO);
        }
        return originName;
    }

    public static TypeMirror getCustomReaderClass(TeleParameterElement teleParam, Elements elementUtils) {

        if (teleParam.getProperty(JsonFieldElement.class) != null) {
            return CodegenUtils.classToTypeMirror(JsonFieldReader.class, elementUtils);
        }

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
