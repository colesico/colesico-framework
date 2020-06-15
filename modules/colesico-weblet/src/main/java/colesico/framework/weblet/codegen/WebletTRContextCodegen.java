package colesico.framework.weblet.codegen;

import colesico.framework.assist.codegen.model.AnnotationToolbox;
import colesico.framework.service.codegen.model.TeleCompElement;
import colesico.framework.service.codegen.model.TeleMethodElement;
import colesico.framework.service.codegen.model.TeleParamElement;
import colesico.framework.service.codegen.model.TeleVarElement;
import colesico.framework.weblet.Origin;
import colesico.framework.weblet.ParamName;
import colesico.framework.weblet.ParamOrigin;
import colesico.framework.weblet.teleapi.OriginFacade;
import colesico.framework.weblet.teleapi.WebletCustomReader;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.TypeName;
import org.apache.commons.lang3.StringUtils;

import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WebletTRContextCodegen {

    private final TeleParamElement teleParam;
    private final Class<?> contextClass;
    private final Class<? extends OriginFacade> originFacadeClass;

    private final TeleMethodElement teleMethod;
    private final CodeBlock.Builder cb = CodeBlock.builder();

    public WebletTRContextCodegen(TeleParamElement teleParam, Class<?> contextClass, Class<? extends OriginFacade> originFacadeClass) {
        this.teleParam = teleParam;
        this.contextClass = contextClass;
        this.originFacadeClass = originFacadeClass;
        teleMethod = teleParam.getParentTeleMethod();
    }

    protected TypeName getCustomReaderClass(TeleVarElement rootVar) {
        AnnotationToolbox<WebletCustomReader> crAnn = teleParam.getOriginVariable().getAnnotation(WebletCustomReader.class);
        if (crAnn == null) {
            crAnn = rootVar.getOriginVariable().getAnnotation(WebletCustomReader.class);
        }
        if (crAnn == null) {
            return null;
        }
        TypeMirror readerClassMirror = crAnn.getValueTypeMirror(a -> a.value());
        return TypeName.get(readerClassMirror);
    }

    public CodeBlock generate() {

        List<String> paramNamesChain = new ArrayList<>();

        TeleVarElement curVar = teleParam;
        TeleVarElement rootVar = teleParam;
        while (curVar != null) {
            String name;
            AnnotationToolbox<ParamName> nameAnn = curVar.getOriginVariable().getAnnotation(ParamName.class);
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

        Origin paramOrigin = Origin.AUTO;

        AnnotationToolbox<ParamOrigin> originAnn = teleParam.getOriginVariable().getAnnotation(ParamOrigin.class);
        if (originAnn == null) {
            originAnn = teleMethod.getProxyMethod().getOriginMethod().getAnnotation(ParamOrigin.class);
            if (originAnn == null) {
                originAnn = rootVar.getOriginVariable().getAnnotation(ParamOrigin.class);
            }
        }

        if (originAnn != null) {
            paramOrigin = originAnn.unwrap().value();
        }

        cb.add("new $T(", ClassName.get(contextClass));
        cb.add("$S", paramName);

        // Param origin
        cb.add(", $T.$N", ClassName.get(originFacadeClass), paramOrigin.name());

        TypeName customReader = getCustomReaderClass(rootVar);
        if (customReader == null) {
            cb.add(", null");
        } else {
            cb.add(", $T.class", customReader);
        }

        cb.add(")");
        return cb.build();
    }
}
