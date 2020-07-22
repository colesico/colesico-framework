package colesico.framework.telehttp.codegen;

import colesico.framework.assist.codegen.model.AnnotationAtom;
import colesico.framework.service.codegen.model.TeleCompElement;
import colesico.framework.service.codegen.model.TeleMethodElement;
import colesico.framework.service.codegen.model.TeleParamElement;
import colesico.framework.service.codegen.model.TeleVarElement;
import colesico.framework.telehttp.Origin;
import colesico.framework.telehttp.ParamName;
import colesico.framework.telehttp.ParamOrigin;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TeleHttpCodegenUtils {

    public static String getParamName(TeleParamElement teleParam) {
        List<String> paramNamesChain = new ArrayList<>();

        TeleVarElement curVar = teleParam;
        TeleVarElement rootVar = teleParam;
        while (curVar != null) {
            String name;
            AnnotationAtom<ParamName> nameAnn = curVar.getOriginVariable().getAnnotation(ParamName.class);
            if (nameAnn != null) {
                name = nameAnn.unwrap().value();
            } else if (curVar instanceof TeleCompElement) {
                name = null;
            } else {
                name = curVar.getOriginVariable().getName();
            }

            if (StringUtils.isNotBlank(name)) {
                paramNamesChain.add(name);
            }
            rootVar = curVar;
            curVar = curVar.getParentVariable();
        }

        Collections.reverse(paramNamesChain);
        String paramName = StringUtils.join(paramNamesChain, "");
        return paramName;
    }

    public static TeleVarElement getRootVar(TeleParamElement teleParam) {
        TeleVarElement curVar = teleParam;
        TeleVarElement rootVar = teleParam;
        while (curVar != null) {
            rootVar = curVar;
            curVar = curVar.getParentVariable();
        }
        return rootVar;
    }

    public static Origin getParamOrigin(TeleParamElement teleParam, TeleVarElement rootVar) {
        TeleMethodElement teleMethod = teleParam.getParentTeleMethod();
        Origin paramOrigin = Origin.AUTO;
        AnnotationAtom<ParamOrigin> originAnn = teleParam.getOriginVariable().getAnnotation(ParamOrigin.class);
        if (originAnn == null) {
            originAnn = teleMethod.getProxyMethod().getOriginMethod().getAnnotation(ParamOrigin.class);
            if (originAnn == null) {

                originAnn = rootVar.getOriginVariable().getAnnotation(ParamOrigin.class);
            }
        }

        if (originAnn != null) {
            paramOrigin = originAnn.unwrap().value();
        }
        return paramOrigin;
    }
}
