package colesico.framework.restlet.codegen.assist;

import colesico.framework.assist.codegen.CodegenUtils;
import colesico.framework.restlet.codegen.model.JsonFieldElement;
import colesico.framework.restlet.teleapi.RestletOrigin;
import colesico.framework.restlet.teleapi.RestletParamReader;
import colesico.framework.restlet.teleapi.reader.JsonFieldReader;
import colesico.framework.service.codegen.model.TeleParameterElement;
import colesico.framework.service.codegen.model.TeleArgumentElement;
import colesico.framework.telehttp.codegen.TeleHttpCodegenUtils;
import com.squareup.javapoet.TypeName;

import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;

public class RestletCodegenUtils {

    public static String getParamName(TeleArgumentElement teleArg) {
        String paramName = TeleHttpCodegenUtils.getParamName(teleArg);
        JsonFieldElement jfe = teleArg.getProperty(JsonFieldElement.class);
        if (jfe != null) {
            paramName = jfe.getName();
        }
        return paramName;
    }

    public static String getOriginName(TeleArgumentElement teleArg) {
        String originName;
        JsonFieldElement jfe = teleArg.getProperty(JsonFieldElement.class);
        if (jfe != null) {
            originName = RestletOrigin.JSON_FIELD;
        } else {
            originName = TeleHttpCodegenUtils.getOriginName(teleArg, RestletOrigin.AUTO);
        }
        return originName;
    }

    public static TypeName getCustomReaderClass(TeleParameterElement teleParam, Elements elementUtils) {

        if (teleParam.getProperty(JsonFieldElement.class) != null) {
            return TypeName.get(CodegenUtils.classToTypeMirror(JsonFieldReader.class, elementUtils));
        }

        var rdAnn = teleParam.getOriginElement().getAnnotation(RestletParamReader.class);
        if (rdAnn == null) {
            rdAnn = teleParam.getParentTeleMethod().getServiceMethod().getOriginMethod().getAnnotation(RestletParamReader.class);
        }
        if (rdAnn == null) {
            return null;
        }
        TypeMirror readerClassMirror = rdAnn.getValueTypeMirror(a -> a.value());
        return TypeName.get(readerClassMirror);
    }

}
