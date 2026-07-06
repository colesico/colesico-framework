package colesico.framework.telehttp.codegen;

import colesico.framework.assist.codegen.model.AnnotationAssist;
import colesico.framework.service.codegen.model.teleapi.TeleParameterElement;
import colesico.framework.service.codegen.model.teleapi.TeleCommandElement;
import colesico.framework.telehttp.ParamName;
import colesico.framework.telehttp.ParamOrigin;

public class TeleHttpCodegenUtils {

    public static String paramName(TeleParameterElement teleParam) {
        AnnotationAssist<ParamName> nameAnn = teleParam.originElement().annotation(ParamName.class);
        if (nameAnn != null) {
            return nameAnn.unwrap().value();
        } else {
            return teleParam.originElement().name();
        }
    }

    public static String originName(TeleParameterElement teleParam, String defaultOrigin) {
        TeleCommandElement teleCommand = teleParam.parentTeleCommand();
        String originName = defaultOrigin;
        AnnotationAssist<ParamOrigin> originAnn = teleParam.originElement().annotation(ParamOrigin.class);
        if (originAnn == null) {
            originAnn = teleCommand.serviceMethod().originMethod().annotation(ParamOrigin.class);
        }

        if (originAnn != null) {
            originName = originAnn.unwrap().value();
        }
        return originName;
    }
}
