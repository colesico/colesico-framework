package colesico.framework.telehttp.codegen;

import colesico.framework.assist.codegen.model.AnnotationAssist;
import colesico.framework.service.codegen.model.teleapi.TeleInputElement;
import colesico.framework.service.codegen.model.teleapi.TeleMethodElement;
import colesico.framework.telehttp.ParamName;
import colesico.framework.telehttp.ParamOrigin;

public class TeleHttpCodegenUtils {

    public static String paramName(TeleInputElement teleInput) {
        AnnotationAssist<ParamName> nameAnn = teleInput.originElement().annotation(ParamName.class);
        if (nameAnn != null) {
            return nameAnn.unwrap().value();
        } else {
            return teleInput.originElement().name();
        }
    }

    public static String originName(TeleInputElement teleInput, String defaultOrigin) {
        TeleMethodElement teleMethod = teleInput.parentTeleMethod();
        String originName = defaultOrigin;
        AnnotationAssist<ParamOrigin> originAnn = teleInput.originElement().annotation(ParamOrigin.class);
        if (originAnn == null) {
            originAnn = teleMethod.serviceMethod().originMethod().annotation(ParamOrigin.class);
        }

        if (originAnn != null) {
            originName = originAnn.unwrap().value();
        }
        return originName;
    }
}
