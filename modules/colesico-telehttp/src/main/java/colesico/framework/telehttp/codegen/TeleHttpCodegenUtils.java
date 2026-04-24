package colesico.framework.telehttp.codegen;

import colesico.framework.assist.codegen.model.AnnotationAssist;
import colesico.framework.service.codegen.model.teleapi.TeleInputElement;
import colesico.framework.service.codegen.model.teleapi.TeleMethodElement;
import colesico.framework.service.codegen.model.teleapi.TeleParameterElement;
import colesico.framework.telehttp.ParamName;
import colesico.framework.telehttp.ParamOrigin;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class TeleHttpCodegenUtils {

    public static String paramName(TeleInputElement teleInput) {
        AnnotationAssist<ParamName> nameAnn = teleInput.originElement().getAnnotation(ParamName.class);
        if (nameAnn != null) {
            return nameAnn.unwrap().value();
        } else {
            return teleInput.originElement().name();
        }
    }

    public static String originName(TeleInputElement teleInput, String defaultOrigin) {
        TeleMethodElement teleMethod = teleInput.parentTeleMethod();
        String originName = defaultOrigin;
        AnnotationAssist<ParamOrigin> originAnn = teleInput.originElement().getAnnotation(ParamOrigin.class);
        if (originAnn == null) {
            originAnn = teleMethod.serviceMethod().originMethod().getAnnotation(ParamOrigin.class);
        }

        if (originAnn != null) {
            originName = originAnn.unwrap().value();
        }
        return originName;
    }
}
