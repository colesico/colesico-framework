package colesico.framework.telehttp.codegen;

import colesico.framework.assist.codegen.model.AnnotationAssist;
import colesico.framework.service.codegen.model.TeleMethodElement;
import colesico.framework.service.codegen.model.TeleParamElement;
import colesico.framework.telehttp.Origin;
import colesico.framework.telehttp.ParamName;
import colesico.framework.telehttp.ParamOrigin;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TeleHttpCodegenUtils {

    public static String getParamName(TeleParamElement teleParam) {
        AnnotationAssist<ParamName> nameAnn = teleParam.getOriginParam().getAnnotation(ParamName.class);
        if (nameAnn != null) {
            return nameAnn.unwrap().value();

        }

        return teleParam.getOriginParam().getName();
    }

    public static Origin getParamOrigin(TeleParamElement teleParam) {
        TeleMethodElement teleMethod = teleParam.getParentTeleMethod();
        Origin paramOrigin = Origin.ORIGIN_AUTO;
        AnnotationAssist<ParamOrigin> originAnn = teleParam.getOriginParam().getAnnotation(ParamOrigin.class);
        if (originAnn == null) {
            originAnn = teleMethod.getServiceMethod().getOriginMethod().getAnnotation(ParamOrigin.class);
        }

        if (originAnn != null) {
            paramOrigin = Origin.of(originAnn.unwrap().value());
        }
        return paramOrigin;
    }
}
