package colesico.framework.telehttp.codegen;

import colesico.framework.assist.codegen.model.AnnotationAssist;
import colesico.framework.service.codegen.model.teleapi.TeleEntryElement;
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

    public static String getParamName(TeleEntryElement entry) {

        List<String> namesChain = new ArrayList<>();
        Iterator<TeleEntryElement> it = entry.getIterator();
        while (it.hasNext()) {
            String paramName;
            TeleEntryElement curArg = it.next();
            AnnotationAssist<ParamName> nameAnn = curArg.getOriginElement().getAnnotation(ParamName.class);
            if (nameAnn != null) {
                paramName = nameAnn.unwrap().value();
            } else {
                if (curArg instanceof TeleParameterElement) {
                    paramName = curArg.getOriginElement().getName();
                } else {
                    paramName = "";
                }
            }
            namesChain.add(paramName);
        }
        Collections.reverse(namesChain);
        return StringUtils.join(namesChain.toArray());
    }

    public static String getOriginName(TeleEntryElement teleArg, String defaultOrigin) {
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
