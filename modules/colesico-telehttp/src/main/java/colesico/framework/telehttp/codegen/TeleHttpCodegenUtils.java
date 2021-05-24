package colesico.framework.telehttp.codegen;

import colesico.framework.assist.codegen.model.AnnotationAssist;
import colesico.framework.service.codegen.model.TeleMethodElement;
import colesico.framework.service.codegen.model.TeleParamElement;
import colesico.framework.telehttp.Origin;
import colesico.framework.telehttp.ParamName;
import colesico.framework.telehttp.ParamOrigin;

public class TeleHttpCodegenUtils {

    public static String getParamName(TeleParamElement teleParam) {
        AnnotationAssist<ParamName> nameAnn = teleParam.getOriginParam().getAnnotation(ParamName.class);
        if (nameAnn != null) {
            return nameAnn.unwrap().value();

        }

        return teleParam.getOriginParam().getName();
    }

    public static String getOriginName(TeleParamElement teleParam, String defaultOrigin) {
        TeleMethodElement teleMethod = teleParam.getParentTeleMethod();
        String originName = defaultOrigin;
        AnnotationAssist<ParamOrigin> originAnn = teleParam.getOriginParam().getAnnotation(ParamOrigin.class);
        if (originAnn == null) {
            originAnn = teleMethod.getServiceMethod().getOriginMethod().getAnnotation(ParamOrigin.class);
        }

        if (originAnn != null) {
            originName = originAnn.unwrap().value();
        }
        return originName;
    }
}
