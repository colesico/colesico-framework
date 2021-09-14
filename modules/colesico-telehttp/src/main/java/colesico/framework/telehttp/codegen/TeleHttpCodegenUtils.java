package colesico.framework.telehttp.codegen;

import colesico.framework.assist.codegen.model.AnnotationAssist;
import colesico.framework.service.codegen.model.TeleMethodElement;
import colesico.framework.service.codegen.model.TeleArgumentElement;
import colesico.framework.telehttp.ParamName;
import colesico.framework.telehttp.ParamOrigin;

public class TeleHttpCodegenUtils {

    public static String getParamName(TeleArgumentElement teleArg) {
        AnnotationAssist<ParamName> nameAnn = teleArg.getOriginElement().getAnnotation(ParamName.class);
        if (nameAnn != null) {
            return nameAnn.unwrap().value();
        }

        return teleArg.getOriginElement().getName();
    }

    public static String getOriginName(TeleArgumentElement teleArg, String defaultOrigin) {
        TeleMethodElement teleMethod = teleArg.getParentTeleMethod();
        String originName = defaultOrigin;
        AnnotationAssist<ParamOrigin> originAnn = teleArg.getOriginElement().getAnnotation(ParamOrigin.class);
        if (originAnn == null) {
            originAnn = teleMethod.getServiceMethod().getOriginMethod().getAnnotation(ParamOrigin.class);
        }

        if (originAnn != null) {
            originName = originAnn.unwrap().value();
        }
        return originName;
    }
}
