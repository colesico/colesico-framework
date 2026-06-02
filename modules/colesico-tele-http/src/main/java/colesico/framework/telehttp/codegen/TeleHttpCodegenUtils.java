package colesico.framework.telehttp.codegen;

import colesico.framework.assist.codegen.model.AnnotationAssist;
import colesico.framework.service.codegen.model.teleapi.TeleParameterElement;
import colesico.framework.service.codegen.model.teleapi.TeleCommandElement;
import colesico.framework.telehttp.ParamName;
import colesico.framework.telehttp.ParamOrigin;

public class TeleHttpCodegenUtils {

    public static String paramName(TeleParameterElement teleInput) {
        AnnotationAssist<ParamName> nameAnn = teleInput.originElement().annotation(ParamName.class);
        if (nameAnn != null) {
            return nameAnn.unwrap().value();
        } else {
            return teleInput.originElement().name();
        }
    }

    public static String originName(TeleParameterElement teleInput, String defaultOrigin) {
        TeleCommandElement teleCommand = teleInput.parentTeleCommand();
        String originName = defaultOrigin;
        AnnotationAssist<ParamOrigin> originAnn = teleInput.originElement().annotation(ParamOrigin.class);
        if (originAnn == null) {
            originAnn = teleCommand.serviceMethod().originMethod().annotation(ParamOrigin.class);
        }

        if (originAnn != null) {
            originName = originAnn.unwrap().value();
        }
        return originName;
    }
}
