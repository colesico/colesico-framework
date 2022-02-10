package colesico.framework.telehttp.codegen;

import colesico.framework.assist.codegen.model.AnnotationAssist;
import colesico.framework.service.codegen.model.TeleMethodElement;
import colesico.framework.service.codegen.model.TeleVarElement;
import colesico.framework.service.codegen.model.TeleParameterElement;
import colesico.framework.telehttp.ParamName;
import colesico.framework.telehttp.ParamOrigin;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class TeleHttpCodegenUtils {

    public static String getParamName(TeleVarElement teleArg) {

        List<String> namesChain = new ArrayList<>();
        Iterator<TeleVarElement> it = (Iterator<TeleVarElement>) teleArg.getIterator();
        while (it.hasNext()) {
            String paramName;
            TeleVarElement curArg = it.next();
            AnnotationAssist<ParamName> nameAnn = curArg.getOriginElement().getAnnotation(ParamName.class);
            if (nameAnn != null) {
                paramName = nameAnn.unwrap().value();
            } else {
                if (curArg instanceof TeleParameterElement) {
                    paramName = curArg.getOriginElement().getName();
                } else {
                    paramName="";
                }
            }
            namesChain.add(paramName);
        }
        Collections.reverse(namesChain);
        return StringUtils.join(namesChain.toArray());
    }

    public static String getOriginName(TeleVarElement teleArg, String defaultOrigin) {
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
